package com.huy.food.repositories;

import com.huy.food.entities.DeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {

    List<DeliveryItem> findByDeliveryId(UUID deliveryId);
}