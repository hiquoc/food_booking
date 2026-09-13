package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.UpdateUserRequest;
import com.huy.food.dtos.responses.UserResponse;
import com.huy.food.enums.AccountRole;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@AuthenticationPrincipal UserPrincipal principal,
                                                              @RequestParam(required = false) String phone) {
        return ApiResponse.ok(userService.getUserResponse(principal.userId(), phone));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@AuthenticationPrincipal UserPrincipal principal,
                                                                 @Valid @RequestBody UpdateUserRequest request) {
        return ApiResponse.ok(userService.updateUser(principal.userId(), request));
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(@AuthenticationPrincipal UserPrincipal principal,
                                                                 @PathVariable UUID userId,
                                                                 @RequestParam AccountRole role) {
        return ApiResponse.ok(userService.updateUserRole(principal.userId(), userId, role));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal UserPrincipal principal) {
        userService.deleteUser(principal.userId());
        return ApiResponse.noContent();
    }
}
