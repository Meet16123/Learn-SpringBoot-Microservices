package com.meet.ecom.order_service.controller;

import com.meet.ecom.order_service.clients.InventoryOpenFeignClient;
import com.meet.ecom.order_service.dto.OrderRequestDto;
import com.meet.ecom.order_service.entity.OrderItem;
import com.meet.ecom.order_service.repository.OrdersRepository;
import com.meet.ecom.order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping("/helloOrders")
    public String helloOrders() {
        return "Hello from Orders";
    }

    @GetMapping("getAll")
    public ResponseEntity<List<OrderRequestDto>> getAllOrders() {
        log.info("Fetching all orders");
        List<OrderRequestDto> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id) {
        log.info("Fetching order by id: {}", id);
        OrderRequestDto order = ordersService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderRequestDto order = ordersService.createOrder(orderRequestDto);

        return ResponseEntity.ok(order);
    }

}
