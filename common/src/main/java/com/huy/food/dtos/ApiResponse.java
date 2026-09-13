package com.huy.food.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(true, status, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, HttpStatus.OK, "Success", data, Instant.now());
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(false, status, message, null, Instant.now());
    }


    // ==========================================
    // 🟢 1. CÁC TRƯỜNG HỢP THÀNH CÔNG (SUCCESS - 2xx)
    // ==========================================

    // HTTP 200 OK: Trả về dữ liệu thành công (Mặc định)
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>(true, HttpStatus.OK, "Success", data, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // HTTP 200 OK: Thành công nhưng không có data trả về (Chỉ thông báo)
    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        ApiResponse<T> response = new ApiResponse<>(true, HttpStatus.OK, "Success", null, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // HTTP 200 OK: Thành công kèm Message tùy biến
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(true, HttpStatus.OK, message, data, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // HTTP 201 Created: Tạo mới tài nguyên thành công (Dùng cho POST)
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        ApiResponse<T> response = new ApiResponse<>(true, HttpStatus.CREATED, "Resource created successfully", data, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // HTTP 201 Created: Tạo mới thành công kèm Message tùy biến
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(true, HttpStatus.CREATED, message, data, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // HTTP 204 No Content: Xóa hoặc cập nhật thành công và không cần trả về Body
    public static <T> ResponseEntity<ApiResponse<T>> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Chuẩn REST: 204 không có Body
    }

    // HTTP 400 Bad Request: Dữ liệu gửi lên sai định dạng, thiếu trường bắt buộc
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        ApiResponse<T> response = new ApiResponse<>(false, HttpStatus.BAD_REQUEST, message, null, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // HTTP 401 Unauthorized: Chưa đăng nhập, token hết hạn hoặc sai token
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        ApiResponse<T> response = new ApiResponse<>(false, HttpStatus.UNAUTHORIZED, message, null, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


}
