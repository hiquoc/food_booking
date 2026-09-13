package com.huy.food.services;

import com.huy.food.dtos.requests.CreateOwnerRequest;
import com.huy.food.dtos.requests.UpdateOwnerRequest;
import com.huy.food.dtos.responses.OwnerResponse;

import java.util.List;
import java.util.UUID;

public interface OwnerService {
    List<OwnerResponse> getOwners();
    OwnerResponse getOwner(UUID ownerId);
    OwnerResponse getOwnerByUserId(UUID userId);
    OwnerResponse createOwner(CreateOwnerRequest request);
    OwnerResponse updateOwner(UUID ownerId, UpdateOwnerRequest request);
    void deleteOwner(UUID ownerId);
}