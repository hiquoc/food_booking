package com.huy.food.services;

import com.huy.food.dtos.requests.UpdateUserRequest;
import com.huy.food.dtos.responses.UserResponse;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse getUserResponse(UUID userId, String phone);
    User getUserById(UUID userId);
    Optional<User> getUserByPhoneWithOpt(String phone);
    User getUserByPhone(String phone);
    User getUserByIdAndRole(UUID ownerId, AccountRole role);
    boolean checkIfUserIsBanned(UUID userId);

    User createUser(String phone,AccountRole role);
    UserResponse updateUser(UUID userId, UpdateUserRequest request);
    void deleteUser(UUID userId);
    UserResponse updateUserRole(UUID requesterId, UUID userId, AccountRole role);

}
