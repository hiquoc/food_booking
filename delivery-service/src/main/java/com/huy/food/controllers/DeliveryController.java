package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateDeliveryRequest;
import com.huy.food.dtos.requests.UpdateDeliveryRequest;
import com.huy.food.dtos.responses.DeliveryResponse;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryResponse>>> allDeliveries() {
        return ApiResponse.ok(service.getDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> delivery(@PathVariable UUID id) {
        return ApiResponse.ok(service.getDelivery(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryResponse>> createDelivery(@Valid @RequestBody CreateDeliveryRequest request,
                                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(service.createDelivery(request,userPrincipal.userId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> updateDelivery(@PathVariable UUID id,
                                                                        @Valid @RequestBody UpdateDeliveryRequest request) {
        return ApiResponse.ok(service.updateDelivery(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDelivery(@PathVariable UUID id) {
        service.deleteDelivery(id);
        return ApiResponse.noContent();
    }

}

