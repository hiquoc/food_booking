package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateDeliveryAssignmentRequest;
import com.huy.food.dtos.requests.UpdateDeliveryAssignmentRequest;
import com.huy.food.dtos.responses.DeliveryAssignmentResponse;
import com.huy.food.services.DeliveryAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-assignments")
@RequiredArgsConstructor
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryAssignmentResponse>>> allAssignments() {
        return ApiResponse.ok(service.getAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> assignment(@PathVariable Long id) {
        return ApiResponse.ok(service.getAssignment(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> createAssignment(@Valid @RequestBody CreateDeliveryAssignmentRequest request) {
        return ApiResponse.created(service.createAssignment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> updateAssignment(@PathVariable Long id,
                                                                                    @Valid @RequestBody UpdateDeliveryAssignmentRequest request) {
        return ApiResponse.ok(service.updateAssignment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        service.deleteAssignment(id);
        return ApiResponse.noContent();
    }
}

