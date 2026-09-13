package com.huy.food.dtos.responses;

import com.huy.food.enums.MenuItemStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemResponse(Long id,
                               UUID restaurantId,
                               Long sectionId,
                               String name,
                               String image,
                               BigDecimal price,
                               BigDecimal discountedPrice,
                               MenuItemStatus status) {
}

