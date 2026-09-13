package com.huy.food.dtos.requests;

import com.huy.food.enums.AssignmentStatus;
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
public class UpdateDeliveryAssignmentRequest {
    private UUID shipperId;
    private Integer sequenceNumber;
    private AssignmentStatus status;
    private Instant expiresAt;
}

