package com.meet.ecom.shipping_service.controller;

import com.meet.ecom.shipping_service.entity.Shipping;
import com.meet.ecom.shipping_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @PostMapping("/confirmShipping")
    public Shipping confirmShipping(@RequestParam Long orderId) {
        Shipping confirmShipping = shippingService.confirmShopping(orderId);
        return confirmShipping;
    }
}
