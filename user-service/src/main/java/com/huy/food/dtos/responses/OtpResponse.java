package com.huy.food.dtos.responses;

import java.time.Instant;

public record OtpResponse(Long id, String phone, String otp, Instant createdAt) {}

