package com.huy.food.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestOtpRequest {
    @NotBlank(message = "Please enter valid phone number")
    private String phone;
}

