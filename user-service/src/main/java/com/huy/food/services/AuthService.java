package com.huy.food.services;

import com.huy.food.dtos.responses.VerifyOtpResponse;
import com.huy.food.enums.AccountRole;

public interface AuthService {
    String requestOtp(String phone);
    VerifyOtpResponse verifyOtp(String otp, String phone, AccountRole roleType);
    VerifyOtpResponse test(String otp, String phone, AccountRole roleType);
}
