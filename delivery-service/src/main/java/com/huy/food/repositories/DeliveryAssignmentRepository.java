package com.huy.food.repositories;

import com.huy.food.entities.DeliveryAssignment;
import com.huy.food.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryAssignmentRepository
        extends JpaRepository<DeliveryAssignment, Long> {

    List<DeliveryAssignment> findByDeliveryId(UUID deliveryId);

    List<DeliveryAssignment> findByShipperId(UUID shipperId);

    List<DeliveryAssignment> findByShipperIdAndStatus(
            UUID shipperId,
            AssignmentStatus status
    );

    List<DeliveryAssignment> findByShipperIdAndStatusIn(
            UUID shipperId,
            List<AssignmentStatus> statuses
    );

    Optional<DeliveryAssignment> findByDeliveryIdAndStatus(
            UUID deliveryId,
            AssignmentStatus status
    );

    List<DeliveryAssignment> findByStatusAndExpiresAtBefore(
            AssignmentStatus status,
            Instant time
    );
}