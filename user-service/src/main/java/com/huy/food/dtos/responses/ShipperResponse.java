package com.huy.food.dtos.responses;

import com.huy.food.enums.AccountRole;

import java.util.UUID;

public record ShipperResponse(UUID id,
                              UUID userId,
                              String name,
                              String phone,
                              AccountRole role,
                              boolean banned) {
}

