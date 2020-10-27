package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SavingsException extends RuntimeException {
    public SavingsException(String message) {
        super(message);
    }
}
