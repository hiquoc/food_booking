package com.huy.food.repositories;

import com.huy.food.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    Optional<Owner> findByUserId(UUID userId);

    Optional<Owner> findByUserPhone(String phone);

    boolean existsByUserId(UUID userId);

    boolean existsByUserPhone(String phone);
}