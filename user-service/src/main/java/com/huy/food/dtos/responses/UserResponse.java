package com.huy.food.dtos.responses;

import com.huy.food.enums.AccountRole;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id,
                           String phone,
                           String name,
                           AccountRole role,
                           boolean banned,
                           Instant createdAt,
                           Instant updatedAt) {
}

