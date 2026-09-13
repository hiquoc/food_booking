package com.huy.food.utils;

import com.huy.food.securities.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationInMs;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    }

    public String generateAccessToken(UUID userId, String phone, String name, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("phone", phone)
                .claim("name",name)
                .claim("role",role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationInMs);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public UserPrincipal validateAndGetPrincipal(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        UUID userId = UUID.fromString(claims.getSubject());
        String phone = claims.get("phone", String.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        return new UserPrincipal(
                userId,
                phone,
                name,
                role
        );
    }


}
