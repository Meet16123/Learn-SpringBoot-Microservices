package com.meet.ecom.order_service.service;

import com.meet.ecom.order_service.clients.InventoryOpenFeignClient;
import com.meet.ecom.order_service.clients.ShippingFeignClient;
import com.meet.ecom.order_service.dto.OrderRequestDto;
import com.meet.ecom.order_service.dto.ShippingDto;
import com.meet.ecom.order_service.entity.OrderItem;
import com.meet.ecom.order_service.entity.OrderStatus;
import com.meet.ecom.order_service.entity.Orders;
import com.meet.ecom.order_service.exceptions.ResourceNotFoundException;
import com.meet.ecom.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;
    private final ShippingFeignClient shippingFeignClient;


    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order by id: {}", id);
        Orders order = ordersRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + id)
        );
        return modelMapper.map(order, OrderRequestDto.class);
    }

    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Creating order: {}", orderRequestDto);
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders order = modelMapper.map(orderRequestDto, Orders.class);

        for(OrderItem orderItem: order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = ordersRepository.save(order);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Error occurred while creating order due to: {}", throwable.getMessage());
        return new OrderRequestDto();
    }

    @Transactional
    public OrderRequestDto cancelOrder(Long orderId) {
        log.info("Cancelling order with id: {}", orderId);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );

        if(orders.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new ResourceNotFoundException("Order already completed cannot be cancelled");
        }

        if(orders.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ResourceNotFoundException("Order already cancelled");
        }

        inventoryOpenFeignClient.revertInventory(orderId);

        orders.setOrderStatus(OrderStatus.CANCELLED);
        Orders savedOrders = ordersRepository.save(orders);

        return modelMapper.map(savedOrders, OrderRequestDto.class);
    }

//    Confirm shipping
    @Transactional
    public ShippingDto confirmShipping(Long orderId) {
        log.info("Confirming shipping for order: {}", orderId);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );

        if(orders.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ResourceNotFoundException("Order already cancelled");
        }

        if(orders.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Order already completed");
        }

        ShippingDto shippingDto = shippingFeignClient.confirmShipping(orderId);

        orders.setOrderStatus(OrderStatus.COMPLETED);
        ordersRepository.save(orders);

        return shippingDto;
    }
}
