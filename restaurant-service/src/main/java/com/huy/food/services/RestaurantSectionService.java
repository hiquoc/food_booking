package com.huy.food.services;

import com.huy.food.dtos.requests.CreateRestaurantSectionRequest;
import com.huy.food.dtos.requests.UpdateRestaurantSectionRequest;
import com.huy.food.dtos.responses.RestaurantSectionResponse;
import com.huy.food.entities.RestaurantSection;

import java.util.List;
import java.util.UUID;

public interface RestaurantSectionService {
    List<RestaurantSectionResponse> getSectionsResponse();
    RestaurantSectionResponse getSectionResponse(Long id);
    RestaurantSectionResponse createSection(CreateRestaurantSectionRequest request);
    RestaurantSectionResponse updateSection(Long id, UpdateRestaurantSectionRequest request, UUID requesterId);
    void deleteSection(Long id, UUID requesterId);

    RestaurantSection getSectionById(Long id);
}
