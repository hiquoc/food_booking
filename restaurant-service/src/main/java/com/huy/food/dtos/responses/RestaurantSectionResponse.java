package com.huy.food.dtos.responses;

import java.util.UUID;

public record RestaurantSectionResponse(Long id, UUID restaurantId, String name) {}

