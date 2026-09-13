package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateDeliveryRequest;
import com.huy.food.dtos.requests.UpdateDeliveryRequest;
import com.huy.food.dtos.responses.DeliveryResponse;
import com.huy.food.entities.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeliveryMapper {

    @Mapping(source = "user.id", target = "userId")
    DeliveryResponse toResponse(Delivery delivery);

    void update(UpdateDeliveryRequest request, @MappingTarget Delivery delivery);

    @Mapping(target = "items", ignore = true)
    Delivery toEntity(CreateDeliveryRequest request);
}

