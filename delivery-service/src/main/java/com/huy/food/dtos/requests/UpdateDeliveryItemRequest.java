package com.huy.food.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryItemRequest {
    private Long menuItemId;
    private String itemName;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer amount;
}

