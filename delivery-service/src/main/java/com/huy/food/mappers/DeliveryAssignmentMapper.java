package com.huy.food.mappers;

import com.huy.food.dtos.requests.UpdateDeliveryAssignmentRequest;
import com.huy.food.dtos.responses.DeliveryAssignmentResponse;
import com.huy.food.entities.DeliveryAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeliveryAssignmentMapper {

    @Mapping(source = "delivery.id", target = "deliveryId")
    @Mapping(source = "shipper.id", target = "shipperId")
    DeliveryAssignmentResponse toResponse(DeliveryAssignment assignment);

    void update(UpdateDeliveryAssignmentRequest request, @MappingTarget DeliveryAssignment assignment);
}

