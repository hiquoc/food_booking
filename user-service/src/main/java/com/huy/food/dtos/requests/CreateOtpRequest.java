package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOtpRequest {
    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "OTP is required")
    private String otp;
}

