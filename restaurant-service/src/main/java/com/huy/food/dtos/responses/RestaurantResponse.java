package com.huy.food.dtos.responses;

import com.huy.food.enums.RestaurantStatus;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record RestaurantResponse(UUID id,
                                 UUID ownerId,
                                 String name,
                                 String phone,
                                 String description,
                                 String address,
                                 String district,
                                 String city,
                                 BigDecimal latitude,
                                 BigDecimal longitude,
                                 LocalTime openHour,
                                 LocalTime closeHour,
                                 RestaurantStatus status) {
}

