package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateRestaurantRequest;
import com.huy.food.dtos.requests.UpdateRestaurantRequest;
import com.huy.food.dtos.responses.RestaurantResponse;
import com.huy.food.enums.RestaurantStatus;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getRestaurants(@RequestParam(required = false) RestaurantStatus status) {
        return ApiResponse.ok(service.getRestaurants(status));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getMyRestaurants(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getRestaurantsByOwner(principal.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurant(@PathVariable UUID id) {
        return ApiResponse.ok(service.getRestaurantResponse(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(@AuthenticationPrincipal UserPrincipal principal,
                                                                            @Valid @RequestBody CreateRestaurantRequest request) {
        return ApiResponse.created(service.createRestaurant(request,  principal.userId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(@PathVariable UUID id,
                                                                            @Valid @RequestBody UpdateRestaurantRequest request,
                                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(service.updateRestaurant(id, request,userPrincipal.userId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable UUID id,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        service.deleteRestaurant(id,userPrincipal.userId());
        return ApiResponse.noContent();
    }
}
