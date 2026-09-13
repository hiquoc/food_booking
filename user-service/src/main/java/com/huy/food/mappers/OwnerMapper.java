package com.huy.food.mappers;

import com.huy.food.dtos.requests.CreateOwnerRequest;
import com.huy.food.dtos.requests.UpdateOwnerRequest;
import com.huy.food.dtos.responses.OwnerResponse;
import com.huy.food.entities.Owner;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OwnerMapper {

    @Mapping(target = "user", ignore = true)
    Owner toEntity(CreateOwnerRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.role", target = "role")
    @Mapping(source = "user.banned", target = "banned")
    OwnerResponse toResponse(Owner owner);

    @Mapping(target = "user", ignore = true)
    void update(UpdateOwnerRequest request, @MappingTarget Owner owner);
}