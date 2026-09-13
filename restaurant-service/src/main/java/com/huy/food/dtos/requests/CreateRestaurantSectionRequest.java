package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantSectionRequest {
    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotBlank(message = "Section name is required")
    private String name;
}

