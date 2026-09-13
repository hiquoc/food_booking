package com.huy.food.services;

import com.huy.food.dtos.requests.CreateDeliveryRequest;
import com.huy.food.dtos.requests.UpdateDeliveryRequest;
import com.huy.food.dtos.responses.DeliveryResponse;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {
    List<DeliveryResponse> getDeliveries();
    DeliveryResponse getDelivery(UUID id);
    DeliveryResponse createDelivery(CreateDeliveryRequest request,UUID userId);
    DeliveryResponse updateDelivery(UUID id, UpdateDeliveryRequest request);
    void deleteDelivery(UUID id);
}

