package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateDeliveryAssignmentRequest;
import com.huy.food.dtos.requests.UpdateDeliveryAssignmentRequest;
import com.huy.food.dtos.responses.DeliveryAssignmentResponse;
import com.huy.food.entities.Delivery;
import com.huy.food.entities.DeliveryAssignment;
import com.huy.food.entities.Shipper;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.DeliveryAssignmentMapper;
import com.huy.food.repositories.DeliveryAssignmentRepository;
import com.huy.food.repositories.DeliveryRepository;
import com.huy.food.services.DeliveryAssignmentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryAssignmentRepository assignmentRepository;
    private final DeliveryAssignmentMapper assignmentMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAssignmentResponse> getAssignments() {
        return assignmentRepository.findAll().stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryAssignmentResponse getAssignment(Long id) {
        return assignmentMapper.toResponse(findAssignmentById(id));
    }

    @Override
    @Transactional
    public DeliveryAssignmentResponse createAssignment(CreateDeliveryAssignmentRequest request) {
        Delivery delivery = findDeliveryById(request.getDeliveryId());
        Shipper shipper = entityManager.getReference(Shipper.class, request.getShipperId());

        DeliveryAssignment assignment = DeliveryAssignment.builder()
                .delivery(delivery)
                .shipper(shipper)
                .sequenceNumber(request.getSequenceNumber())
                .status(request.getStatus() != null ? request.getStatus() : com.huy.food.enums.AssignmentStatus.OFFERED)
                .expiresAt(request.getExpiresAt())
                .build();

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional
    public DeliveryAssignmentResponse updateAssignment(Long id, UpdateDeliveryAssignmentRequest request) {
        DeliveryAssignment assignment = findAssignmentById(id);
        assignmentMapper.update(request, assignment);

        if (request.getShipperId() != null) {
            Shipper shipper = entityManager.getReference(Shipper.class, request.getShipperId());
            assignment.setShipper(shipper);
        }

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        DeliveryAssignment assignment = findAssignmentById(id);
        assignment.softDelete(null);
        assignmentRepository.save(assignment);
    }

    private Delivery findDeliveryById(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
    }

    private DeliveryAssignment findAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery assignment not found"));
    }
}

