package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistCategoryException extends RuntimeException {
    public NotExistCategoryException(String message) {
        super(message);
    }
}
