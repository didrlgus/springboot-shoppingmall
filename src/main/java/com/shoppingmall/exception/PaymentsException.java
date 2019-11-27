package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PaymentsException extends RuntimeException {
    public PaymentsException(String message) {
        super(message);
    }
}
