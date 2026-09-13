package com.huy.food.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException() {super(HttpStatus.NOT_FOUND,"The requested resource was not found");}
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
