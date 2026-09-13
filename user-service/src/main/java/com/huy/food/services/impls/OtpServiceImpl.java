package com.huy.food.services.impls;

import com.huy.food.dtos.requests.UpdateOtpRequest;
import com.huy.food.dtos.responses.OtpResponse;
import com.huy.food.entities.Otp;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.OtpMapper;
import com.huy.food.repositories.OtpRepository;
import com.huy.food.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final OtpMapper otpMapper;

    @Override
    public Optional<Otp> findByPhone(String phone) {
        return otpRepository.findByPhoneAndCreatedAtAfter(phone, Instant.now().minusSeconds(60));
    }

    @Override
    public Otp save(String otp, String phone) {
        Otp newOtp = Otp.builder()
                .phone(phone)
                .otp(otp)
                .build();
        return otpRepository.save(newOtp);
    }

    @Override
    public Otp isValid(String otp, String phone) {
        Optional<Otp> existedOtp= findByPhone(phone);
        if(existedOtp.isEmpty() || !existedOtp.get().getOtp().equals(otp)){
            throw new BadRequestException("Invalid OTP.");
        }
        return existedOtp.get();
    }

    @Override
    @Transactional
    public void deleteOtp(Long otpId) {
        Otp otp = getEntity(otpId);
        otp.softDelete(null);
        otpRepository.save(otp);
    }

    private Otp getEntity(Long otpId) {
        return otpRepository.findById(otpId)
                .orElseThrow(() -> new NotFoundException("OTP not found"));
    }
}
