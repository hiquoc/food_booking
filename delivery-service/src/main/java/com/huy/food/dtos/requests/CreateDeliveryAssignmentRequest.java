package com.huy.food.dtos.requests;

import com.huy.food.enums.AssignmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryAssignmentRequest {
    @NotNull(message = "Delivery ID is required")
    private UUID deliveryId;

    @NotNull(message = "Shipper ID is required")
    private UUID shipperId;

    private Integer sequenceNumber;
    private AssignmentStatus status;
    private Instant expiresAt;
}

