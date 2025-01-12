package com.meet.ecom.order_service.dto;

import lombok.Data;

@Data
public class ShippingDto {
    private Long orderId;
    private String shippingStatus;
}
