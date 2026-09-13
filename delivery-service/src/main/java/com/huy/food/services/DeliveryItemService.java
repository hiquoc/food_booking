package com.huy.food.services;

import com.huy.food.dtos.requests.CreateDeliveryItemRequest;
import com.huy.food.dtos.requests.UpdateDeliveryItemRequest;
import com.huy.food.dtos.responses.DeliveryItemResponse;

import java.util.List;

public interface DeliveryItemService {
    List<DeliveryItemResponse> getItems();
    DeliveryItemResponse getItem(Long id);
    DeliveryItemResponse createItem(CreateDeliveryItemRequest request);
    DeliveryItemResponse updateItem(Long id, UpdateDeliveryItemRequest request);
    void deleteItem(Long id);
}

