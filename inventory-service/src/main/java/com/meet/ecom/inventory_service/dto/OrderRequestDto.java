package com.meet.ecom.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    private List<OrderRequestItemDto> items;
}
