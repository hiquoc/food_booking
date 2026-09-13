package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateRestaurantSectionRequest;
import com.huy.food.dtos.requests.UpdateRestaurantSectionRequest;
import com.huy.food.dtos.responses.RestaurantSectionResponse;
import com.huy.food.entities.RestaurantSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RestaurantSectionMapper {

    RestaurantSection toEntity(CreateRestaurantSectionRequest request);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    RestaurantSectionResponse toResponse(RestaurantSection section);

    void update(UpdateRestaurantSectionRequest request, @MappingTarget RestaurantSection section);

}

