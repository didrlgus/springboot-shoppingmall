package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CatCdException extends RuntimeException {
    public CatCdException(String message) {
        super(message);
    }
}
