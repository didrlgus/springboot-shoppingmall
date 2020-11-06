package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistOrderException extends RuntimeException {
    public NotExistOrderException(String msg) {
        super(msg);
    }
}
