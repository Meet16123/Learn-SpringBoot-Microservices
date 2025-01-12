package com.meet.ecom.order_service.clients;

import com.meet.ecom.order_service.dto.ShippingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shipping-service", path = "/shipping")
public interface ShippingFeignClient {
    @PostMapping("/confirmShipping")
    ShippingDto confirmShipping(@RequestParam("orderId") Long orderId);
}
