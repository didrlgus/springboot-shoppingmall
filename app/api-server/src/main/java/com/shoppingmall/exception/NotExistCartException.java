package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistCartException extends RuntimeException {
    public NotExistCartException(String msg) {
        super(msg);
    }
}
