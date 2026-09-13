package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateShipperRequest;
import com.huy.food.dtos.requests.UpdateShipperRequest;
import com.huy.food.dtos.responses.ShipperResponse;
import com.huy.food.dtos.locations.ShipperLocation;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.ShipperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shippers")
@RequiredArgsConstructor
public class ShipperController {
    private final ShipperService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShipperResponse>>> getShippers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(service.getShippers());
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ShipperResponse>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getShipperByUserId(principal.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipperResponse>> getShipper(@PathVariable UUID id) {
        return ApiResponse.ok(service.getShipper(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ShipperResponse>> createShipper(@Valid @RequestBody CreateShipperRequest request) {
        return ApiResponse.created(service.createShipper(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipperResponse>> updateShipper(@PathVariable UUID id,
                                                                      @Valid @RequestBody UpdateShipperRequest request) {
        return ApiResponse.ok(service.updateShipper(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShipper(@PathVariable UUID id) {
        service.deleteShipper(id);
        return ApiResponse.noContent();
    }

    @MessageMapping("/location")
    public void sendLocation(
            ShipperLocation location,
            Principal principal
    ) {
        Authentication authentication = (Authentication) principal;
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        assert userPrincipal != null;
        service.sendLocation(userPrincipal.userId(), location);
    }
}
