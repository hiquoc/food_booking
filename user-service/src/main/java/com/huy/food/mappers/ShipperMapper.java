package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateShipperRequest;
import com.huy.food.dtos.requests.UpdateShipperRequest;
import com.huy.food.dtos.responses.ShipperResponse;
import com.huy.food.entities.Shipper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShipperMapper {

    @Mapping(target = "user", ignore = true)
    Shipper toEntity(CreateShipperRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.role", target = "role")
    @Mapping(source = "user.banned", target = "banned")
    ShipperResponse toResponse(Shipper shipper);

    @Mapping(target = "user", ignore = true)
    void update(UpdateShipperRequest request, @MappingTarget Shipper shipper);
}
