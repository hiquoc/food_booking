package com.huy.food.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huy.food.dtos.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(@NonNull HttpServletRequest request,
                       HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.error(HttpStatus.FORBIDDEN,"Access is forbidden.");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
