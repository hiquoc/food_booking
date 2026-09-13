package com.huy.food.services;

import com.huy.food.dtos.requests.CreateDeliveryAssignmentRequest;
import com.huy.food.dtos.requests.UpdateDeliveryAssignmentRequest;
import com.huy.food.dtos.responses.DeliveryAssignmentResponse;

import java.util.List;

public interface DeliveryAssignmentService {
    List<DeliveryAssignmentResponse> getAssignments();
    DeliveryAssignmentResponse getAssignment(Long id);
    DeliveryAssignmentResponse createAssignment(CreateDeliveryAssignmentRequest request);
    DeliveryAssignmentResponse updateAssignment(Long id, UpdateDeliveryAssignmentRequest request);
    void deleteAssignment(Long id);
}

