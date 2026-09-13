package com.huy.food.exceptions;

import com.huy.food.dtos.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.warn("request_rejected reason=access_denied path={} message={}",
                path(request), ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.FORBIDDEN, "Access is forbidden."), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String detailMessage = "Validation failed.";
        log.warn("validation_failed statusCode={} path={} field={} message={}",
                "VALIDATION_ERROR",
                path(request),
                fieldError != null ? fieldError.getField() : null,
                fieldError != null ? fieldError.getDefaultMessage() : null);

        return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, detailMessage));
    }

    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequest(Exception ex, WebRequest request) {
        log.warn("invalid_request statusCode={} path={} exceptionClass={} message={}",
                "VALIDATION_ERROR",
                path(request),
                ex.getClass().getName(),
                ex.getMessage());

        return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Validation failed."));
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(AppException e, WebRequest request) {
        log.warn("error_happened code={} path={} message={}",
                e.getStatus(), path(request), e.getMessage());
        return new ResponseEntity<>(ApiResponse.error(e.getStatus(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex, WebRequest request) {
        HttpServletRequest servletRequest = request instanceof ServletWebRequest servletWebRequest
                ? servletWebRequest.getRequest()
                : null;
        String path = servletRequest != null ? servletRequest.getRequestURI() : request.getDescription(false);
        String method = servletRequest != null ? servletRequest.getMethod() : "unknown";
        log.error("unhandled_request_error  method={} path={} exceptionClass={} message={}",
                method,
                path,
                ex.getClass().getName(),
                ex.getMessage(),
                ex);
        return new ResponseEntity<>(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String path(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        String description = request.getDescription(false);
        return description.startsWith("uri=") ? description.substring(4) : description;
    }
}
