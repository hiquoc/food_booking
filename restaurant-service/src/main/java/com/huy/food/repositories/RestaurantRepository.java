package com.huy.food.repositories;

import com.huy.food.dtos.responses.RestaurantResponse;
import com.huy.food.entities.Restaurant;
import com.huy.food.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByPhone(String phone);

    boolean existsByPhone(String phone);

    List<Restaurant> findByStatus(RestaurantStatus status);

    List<Restaurant> findByOwnerId(UUID ownerId);

    Optional<Restaurant> findByIdAndOwnerId(UUID id, UUID ownerId);
}