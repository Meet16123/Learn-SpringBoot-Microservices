package com.meet.ecom.inventory_service.clients;

import com.meet.ecom.inventory_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/orders")
public interface OrdersFeignClient {

    @GetMapping("/core/helloOrders")
    String helloOrders();

    @GetMapping("/core/{id}")
    OrderRequestDto getOrderById(@PathVariable("id") Long id);

}
