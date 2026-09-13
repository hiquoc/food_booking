package com.huy.food.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 1. Định nghĩa cơ chế nạp Access Token (qua Header Authorization: Bearer <Token>)
        SecurityScheme accessTokenScheme = new SecurityScheme()
                .name("AccessToken")
                .description("Nhập Access Token (Không cần ghi chữ 'Bearer ', chỉ cần nhập chuỗi token)")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        // 2. Định nghĩa cơ chế nạp Refresh Token
        SecurityScheme refreshTokenScheme = new SecurityScheme()
                .name("RefreshToken")
                .description("Nhập Refresh Token của bạn tại đây")
                // HOẶC nếu bạn truyền qua COOKIE (Bỏ comment 3 dòng dưới và comment 3 dòng trên nếu dùng cookie)
                 .type(SecurityScheme.Type.APIKEY)
                 .in(SecurityScheme.In.COOKIE)
                 .name("refreshToken") // Tên của Cookie chứa refresh token
                ;

        // 3. Áp dụng cả 2 cơ chế bảo mật này làm cấu hình mặc định (Global) cho toàn bộ API trong Swagger
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("AccessToken")
                .addList("RefreshToken");

        return new OpenAPI()
                .info(new Info()
                        .title("Food Delivery API")
                        .version("1.0.0")
                        .description("Tài liệu hướng dẫn gọi API cho hệ thống đặt đồ ăn"))
                .components(new Components()
                        .addSecuritySchemes("AccessToken", accessTokenScheme)
                        .addSecuritySchemes("RefreshToken", refreshTokenScheme))
                .security(List.of(securityRequirement));
    }
}