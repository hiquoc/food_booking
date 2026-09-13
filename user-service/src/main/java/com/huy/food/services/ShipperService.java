package com.huy.food.services;

import com.huy.food.dtos.requests.CreateShipperRequest;
import com.huy.food.dtos.requests.UpdateShipperRequest;
import com.huy.food.dtos.responses.ShipperResponse;
import com.huy.food.dtos.locations.ShipperLocation;

import java.util.List;
import java.util.UUID;

public interface ShipperService {
    List<ShipperResponse> getShippers();
    ShipperResponse getShipper(UUID shipperId);
    ShipperResponse getShipperByUserId(UUID userId);
    ShipperResponse createShipper(CreateShipperRequest request);
    ShipperResponse updateShipper(UUID shipperId, UpdateShipperRequest request);
    void deleteShipper(UUID shipperId);

    void sendLocation(UUID shipperId, ShipperLocation message);
}
