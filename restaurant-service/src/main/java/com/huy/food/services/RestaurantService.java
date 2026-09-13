package com.huy.food.services;

import com.huy.food.dtos.requests.CreateRestaurantRequest;
import com.huy.food.dtos.requests.UpdateRestaurantRequest;
import com.huy.food.dtos.responses.RestaurantResponse;
import com.huy.food.entities.Restaurant;
import com.huy.food.enums.RestaurantStatus;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantResponse getRestaurantResponse(UUID restaurantId);
    Restaurant getRestaurantById(UUID restaurantId);
    List<RestaurantResponse> getRestaurantsByOwner(UUID ownerId);
    RestaurantResponse createRestaurant(CreateRestaurantRequest request, UUID ownerId);
    RestaurantResponse updateRestaurant(UUID restaurantId, UpdateRestaurantRequest request, UUID requesterId);
    void deleteRestaurant(UUID restaurantId, UUID requesterId);

    List<RestaurantResponse> getRestaurants(RestaurantStatus status);
}
