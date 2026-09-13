package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateRestaurantRequest;
import com.huy.food.dtos.requests.UpdateRestaurantRequest;
import com.huy.food.dtos.responses.RestaurantResponse;
import com.huy.food.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RestaurantMapper {
    @Mapping(target = "owner", ignore = true)
    Restaurant toEntity(CreateRestaurantRequest request);

    @Mapping(source = "owner.id", target = "ownerId")
    RestaurantResponse toResponse(Restaurant restaurant);

    void update(UpdateRestaurantRequest request, @MappingTarget Restaurant restaurant);
}
