package com.huy.food.repositories;

import com.huy.food.entities.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipperRepository extends JpaRepository<Shipper, UUID> {

    Optional<Shipper> findByUserId(UUID userId);

    Optional<Shipper> findByUserPhone(String phone);

    boolean existsByUserId(UUID userId);

    boolean existsByUserPhone(String phone);
}