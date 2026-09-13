package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateOwnerRequest;
import com.huy.food.dtos.requests.UpdateOwnerRequest;
import com.huy.food.dtos.responses.OwnerResponse;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OwnerResponse>>> getOwners() {
        return ApiResponse.ok(ownerService.getOwners());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<OwnerResponse>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(ownerService.getOwnerByUserId(principal.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OwnerResponse>> getOwner(@PathVariable UUID id) {
        return ApiResponse.ok(ownerService.getOwner(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<OwnerResponse>> createOwner(@Valid @RequestBody CreateOwnerRequest request) {
        return ApiResponse.created(ownerService.createOwner(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OwnerResponse>> updateOwner(@PathVariable UUID id,
                                                                  @Valid @RequestBody UpdateOwnerRequest request) {
        return ApiResponse.ok(ownerService.updateOwner(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable UUID id) {
        ownerService.deleteOwner(id);
        return ApiResponse.noContent();
    }
}