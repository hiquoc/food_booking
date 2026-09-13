package com.huy.food.repositories;

import com.huy.food.entities.Delivery;
import com.huy.food.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByCode(String code);

    List<Delivery> findByUserId(UUID userId);

    List<Delivery> findByUserIdAndStatus(
            UUID userId,
            DeliveryStatus status
    );

    List<Delivery> findByStatus(DeliveryStatus status);

    boolean existsByCode(String code);

     int countByUserIdAndStatusNotIn(UUID userId, List<DeliveryStatus> statuses);
}