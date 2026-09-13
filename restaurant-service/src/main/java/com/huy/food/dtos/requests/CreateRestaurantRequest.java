package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantRequest {
    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String address;
    private String district;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalTime openHour;
    private LocalTime closeHour;
}

