package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateRestaurantSectionRequest;
import com.huy.food.dtos.requests.UpdateRestaurantSectionRequest;
import com.huy.food.dtos.responses.RestaurantSectionResponse;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.RestaurantSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant-sections")
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
@RequiredArgsConstructor
public class RestaurantSectionController {
    private final RestaurantSectionService service;

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantSectionResponse>>> getSections() {
        return ApiResponse.ok(service.getSectionsResponse());
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantSectionResponse>> getSection(@PathVariable Long id) {
        return ApiResponse.ok(service.getSectionResponse(id));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantSectionResponse>> createSection(@Valid @RequestBody CreateRestaurantSectionRequest request) {
        return ApiResponse.created(service.createSection(request));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantSectionResponse>> updateSection(@PathVariable Long id,
                                                                                @Valid @RequestBody UpdateRestaurantSectionRequest request,
                                                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(service.updateSection(id, request,userPrincipal.userId()));
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSection(@PathVariable Long id,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        service.deleteSection(id,userPrincipal.userId());
        return ApiResponse.noContent();
    }
}
