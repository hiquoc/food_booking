package com.huy.food.controllers;

import com.huy.food.dtos.ApiResponse;
import com.huy.food.dtos.requests.RequestOtpRequest;
import com.huy.food.dtos.requests.UpdateOtpRequest;
import com.huy.food.dtos.requests.VerifyOtpRequest;
import com.huy.food.dtos.responses.OtpResponse;
import com.huy.food.dtos.responses.VerifyOtpResponse;
import com.huy.food.enums.AccountRole;
import com.huy.food.services.AuthService;
import com.huy.food.services.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> test( @RequestBody String phone,
                                                         @RequestParam(required = false,defaultValue = "USER") AccountRole roleType,
                                                         HttpServletResponse httpServletResponse) {
        VerifyOtpResponse response = authService.test("111111", phone,roleType);
        setRefreshTokenCookie(httpServletResponse, response.refreshToken());
        return ApiResponse.ok(response.accessToken());
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<String>> requestOtp(@Valid @RequestBody RequestOtpRequest request) {
        return ApiResponse.ok(authService.requestOtp(request.getPhone()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request,
                                                         @RequestParam(required = false,defaultValue = "USER") AccountRole roleType,
                                                         HttpServletResponse httpServletResponse) {
        VerifyOtpResponse response = authService.verifyOtp(request.getOtp(), request.getPhone(),roleType);
        setRefreshTokenCookie(httpServletResponse, response.refreshToken());
        return ApiResponse.ok(response.accessToken());
    }

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationInMs;

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Change to true in prod
                .path("/")
                .maxAge(refreshExpirationInMs / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
