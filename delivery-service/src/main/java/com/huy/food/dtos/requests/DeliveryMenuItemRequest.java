package com.huy.food.dtos.requests;

import com.huy.food.enums.DeliveryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMenuItemRequest {
    @NotNull(message = "ItemId is required")
    private Long itemId;
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Version is required")
    private Integer version;
}

