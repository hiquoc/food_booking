package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateDeliveryItemRequest;
import com.huy.food.dtos.requests.UpdateDeliveryItemRequest;
import com.huy.food.dtos.responses.DeliveryItemResponse;
import com.huy.food.services.DeliveryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-items")
@RequiredArgsConstructor
public class DeliveryItemController {

    private final DeliveryItemService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryItemResponse>>> allItems() {
        return ApiResponse.ok(service.getItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryItemResponse>> item(@PathVariable Long id) {
        return ApiResponse.ok(service.getItem(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryItemResponse>> createItem(@Valid @RequestBody CreateDeliveryItemRequest request) {
        return ApiResponse.created(service.createItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryItemResponse>> updateItem(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateDeliveryItemRequest request) {
        return ApiResponse.ok(service.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ApiResponse.noContent();
    }
}

