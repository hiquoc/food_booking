package com.huy.food.mappers;

import com.huy.food.dtos.requests.UpdateUserRequest;
import com.huy.food.dtos.responses.UserResponse;
import com.huy.food.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponse toResponse(User user);

    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget User user);
}
