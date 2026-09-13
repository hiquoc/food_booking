package com.huy.food.services;

import com.huy.food.dtos.requests.UpdateOtpRequest;
import com.huy.food.dtos.responses.OtpResponse;
import com.huy.food.entities.Otp;

import java.util.List;
import java.util.Optional;

public interface OtpService {
    Optional<Otp> findByPhone(String phone);
    Otp save(String otp, String phone);
    Otp isValid(String otp, String phone);
    void deleteOtp(Long otpId);
}
