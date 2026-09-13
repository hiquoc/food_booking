package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryItemRequest {
    @NotNull(message = "Delivery ID is required")
    private UUID deliveryId;

    private Long menuItemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private BigDecimal discountedRate;

    @NotNull(message = "Amount is required")
    private Integer amount;
}

