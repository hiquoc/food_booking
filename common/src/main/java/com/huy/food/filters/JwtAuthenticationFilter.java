package com.huy.food.filters;

import com.huy.food.securities.UserPrincipal;
import com.huy.food.utils.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final JwtTokenProvider jwtTokenProvider;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            UserPrincipal userPrincipal = jwtTokenProvider.validateAndGetPrincipal(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userPrincipal,null, List.of(new SimpleGrantedAuthority("ROLE_"+userPrincipal.role())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.warn("Token is invalid");
        }
        filterChain.doFilter(request, response);
    }
}
