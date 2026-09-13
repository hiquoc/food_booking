package com.huy.food.dtos.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DeliveryItemResponse(Long id,
                                   UUID deliveryId,
                                   Long menuItemId,
                                   String itemName,
                                   BigDecimal price,
                                   BigDecimal discountedPrice,
                                   Integer amount,
                                   Instant createdAt,
                                   Instant updatedAt) {
}

