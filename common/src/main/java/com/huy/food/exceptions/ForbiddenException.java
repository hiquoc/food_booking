package com.huy.food.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException {
    public ForbiddenException() {super(HttpStatus.FORBIDDEN,"You are not allowed to perform this action");}
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
