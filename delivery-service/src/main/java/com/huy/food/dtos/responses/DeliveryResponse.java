package com.huy.food.dtos.responses;

import com.huy.food.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DeliveryResponse(UUID id,
                               String code,
                               UUID userId,
                               DeliveryStatus status,
                               BigDecimal subtotal,
                               BigDecimal discountAmount,
                               BigDecimal shippingFee,
                               BigDecimal totalAmount,
                               BigDecimal distance,
                               BigDecimal shipperRevenue,
                               Instant expectedDeliveryAt,
                               Instant deliveredAt,
                               Instant createdAt,
                               Instant updatedAt) {
}

