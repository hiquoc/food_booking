package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateMenuItemRequest;
import com.huy.food.dtos.requests.UpdateMenuItemRequest;
import com.huy.food.dtos.responses.MenuItemResponse;
import com.huy.food.entities.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MenuItemMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "section.id", target = "sectionId")
    MenuItemResponse toResponse(MenuItem menuItem);

    @Mapping(target = "displayOrder", ignore = true)
    void update(UpdateMenuItemRequest request, @MappingTarget MenuItem menuItem);

    MenuItem toEntity(CreateMenuItemRequest request);
}

