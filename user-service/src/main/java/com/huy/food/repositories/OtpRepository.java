package com.huy.food.repositories;

import com.huy.food.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findByPhone(String phone);


    Optional<Otp> findByPhoneAndCreatedAtAfter(String phone, Instant instant);
}
