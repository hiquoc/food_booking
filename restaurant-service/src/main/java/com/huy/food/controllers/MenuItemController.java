package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.CreateMenuItemRequest;
import com.huy.food.dtos.requests.UpdateMenuItemRequest;
import com.huy.food.dtos.responses.MenuItemResponse;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getMenuItems() {
        return ApiResponse.ok(service.getMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItem(@PathVariable Long id) {
        return ApiResponse.ok(service.getMenuItem(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
        return ApiResponse.created(service.createMenuItem(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateMenuItemRequest request) {
        return ApiResponse.ok(service.updateMenuItem(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id,
                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        service.deleteMenuItem(id,userPrincipal.userId());
        return ApiResponse.noContent();
    }
}
