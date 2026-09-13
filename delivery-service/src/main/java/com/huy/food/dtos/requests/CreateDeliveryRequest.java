package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryRequest {
    @NotNull(message = "RestaurantId is required")
    private UUID restaurantId;
    @NotNull(message = "Menu items are required")
    private List<DeliveryMenuItemRequest> items;
}
