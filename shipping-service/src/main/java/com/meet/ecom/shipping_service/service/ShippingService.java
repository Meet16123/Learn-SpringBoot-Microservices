package com.meet.ecom.shipping_service.service;

import com.meet.ecom.shipping_service.entity.Shipping;
import com.meet.ecom.shipping_service.entity.ShippingStatus;
import com.meet.ecom.shipping_service.repository.ShippingRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class ShippingService {

    private final ShippingRepository shippingRepository;

    public Shipping confirmShopping(Long orderId) {
        log.info("Confirming shipping for order id: {}", orderId);
        Shipping shipping = Shipping.builder()
                .orderId(orderId)
                .shippingStatus(ShippingStatus.DELIVERED)
                .build();

        Shipping savedShipping = shippingRepository.save(shipping);

        return savedShipping;
    }
}

