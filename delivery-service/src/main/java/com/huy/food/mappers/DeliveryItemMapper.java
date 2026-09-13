package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateDeliveryItemRequest;
import com.huy.food.dtos.requests.UpdateDeliveryItemRequest;
import com.huy.food.dtos.responses.DeliveryItemResponse;
import com.huy.food.entities.DeliveryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeliveryItemMapper {

    @Mapping(source = "delivery.id", target = "deliveryId")
    @Mapping(source = "menuItem.id", target = "menuItemId")
    DeliveryItemResponse toResponse(DeliveryItem deliveryItem);

    void update(UpdateDeliveryItemRequest request, @MappingTarget DeliveryItem deliveryItem);


    DeliveryItem toEntity(CreateDeliveryItemRequest request);
}

