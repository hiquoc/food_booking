package com.huy.food.repositories;

import com.huy.food.entities.RestaurantSection;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantSectionRepository
        extends JpaRepository<RestaurantSection, Long> {

    List<RestaurantSection> findByRestaurantId(UUID restaurantId);

    boolean existsByRestaurantIdAndName(
            UUID restaurantId,
            String name
    );

    long countRestaurantSectionByRestaurantId(UUID restaurantId);
}