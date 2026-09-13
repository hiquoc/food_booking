package com.huy.food.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantRequest {
    private String phone;
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

