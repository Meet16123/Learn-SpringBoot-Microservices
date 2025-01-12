package com.meet.ecom.order_service.clients;

import com.meet.ecom.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryOpenFeignClient {

    @PutMapping("/products/reduce-stocks")
    Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);

    @PostMapping("/products/revert-stock")
    void revertInventory(@RequestParam("orderId") Long orderId);
}
