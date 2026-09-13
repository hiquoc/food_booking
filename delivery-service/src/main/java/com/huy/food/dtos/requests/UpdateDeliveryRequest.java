package com.huy.food.dtos.requests;

import com.huy.food.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryRequest {
    private DeliveryStatus status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private BigDecimal distance;
    private BigDecimal shipperRevenue;
    private Instant expectedDeliveryAt;
    private Instant deliveredAt;
}

