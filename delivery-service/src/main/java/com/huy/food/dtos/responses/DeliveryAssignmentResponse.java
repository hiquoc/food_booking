package com.huy.food.dtos.responses;

import com.huy.food.enums.AssignmentStatus;

import java.time.Instant;
import java.util.UUID;

public record DeliveryAssignmentResponse(Long id,
                                         UUID deliveryId,
                                         UUID shipperId,
                                         Integer sequenceNumber,
                                         AssignmentStatus status,
                                         Instant expiresAt,
                                         Instant createdAt,
                                         Instant updatedAt) {
}

