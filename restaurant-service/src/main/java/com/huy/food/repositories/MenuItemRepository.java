package com.huy.food.repositories;

import com.huy.food.entities.MenuItem;
import com.huy.food.enums.MenuItemStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(UUID restaurantId);

    List<MenuItem> findByRestaurantIdAndStatus(
            UUID restaurantId,
            MenuItemStatus status
    );

    List<MenuItem> findBySectionId(Long sectionId);

    long countMenuItemBySectionId(@NotNull(message = "Section ID is required") Long sectionId);

    @EntityGraph(attributePaths = {"restaurant"})
    List<MenuItem> findByIdInAndRestaurantId(Collection<Long> id, UUID restaurantId);
}