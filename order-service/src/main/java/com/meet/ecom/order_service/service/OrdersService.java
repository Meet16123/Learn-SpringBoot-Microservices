package com.meet.ecom.order_service.service;

import com.meet.ecom.order_service.clients.InventoryOpenFeignClient;
import com.meet.ecom.order_service.dto.OrderRequestDto;
import com.meet.ecom.order_service.entity.OrderItem;
import com.meet.ecom.order_service.entity.OrderStatus;
import com.meet.ecom.order_service.entity.Orders;
import com.meet.ecom.order_service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;


    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order by id: {}", id);
        Orders order = ordersRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id: " + id)
        );
        return modelMapper.map(order, OrderRequestDto.class);
    }

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
}
