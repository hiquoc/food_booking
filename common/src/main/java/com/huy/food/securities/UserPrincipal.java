package com.huy.food.securities;

import java.util.UUID;

public record UserPrincipal (UUID userId,String phone,String name,String role) {
}
