package com.huy.food.services.impls;

import com.huy.food.dtos.responses.VerifyOtpResponse;
import com.huy.food.entities.Otp;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.repositories.UserRepository;
import com.huy.food.services.OtpService;
import com.huy.food.services.AuthService;
import com.huy.food.services.UserService;
import com.huy.food.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final OtpService otpService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public String requestOtp(String phone) {
        Optional<Otp> existedOtp= otpService.findByPhone(phone);
        if(existedOtp.isPresent()){
            throw new BadRequestException("An OTP already sent to your phone.");
        }
        Otp otp = otpService.save("111111",phone);
        return otp.getOtp();
    }

    @Override
    public VerifyOtpResponse verifyOtp(String otp, String phone, AccountRole roleType) {
        otpService.isValid(otp, phone);

        User user = userService.getUserByPhoneWithOpt(phone)
                .orElseGet(() -> createUserForLogin(phone, roleType));

        validateLoginRole(user, roleType);

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getPhone(),
                user.getName(),
                user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new VerifyOtpResponse(accessToken, refreshToken);
    }

    @Override
    public VerifyOtpResponse test(String otp, String phone, AccountRole roleType) {
        User user = userService.getUserByPhoneWithOpt(phone)
                .orElseGet(() -> createUserForLogin(phone, roleType));

        validateLoginRole(user, roleType);

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getPhone(),
                user.getName(),
                user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new VerifyOtpResponse(accessToken, refreshToken);
    }

    private User createUserForLogin(String phone, AccountRole roleType) {
        if (roleType != AccountRole.USER) {
            throwUnauthorizedLogin(phone);
        }

        return userService.createUser(phone, AccountRole.USER);
    }

    private void validateLoginRole(User user, AccountRole roleType) {
        if (roleType == AccountRole.USER) {
            return;
        }
        if (user.getRole() != roleType) {
            throwUnauthorizedLogin(user.getPhone());
        }
    }

    private void throwUnauthorizedLogin(String phone){
        log.warn("Unauthorized user login shipping application, phone: {}", phone);
        throw new BadRequestException("You are not allowed to login into shipping application.");
    }


}
