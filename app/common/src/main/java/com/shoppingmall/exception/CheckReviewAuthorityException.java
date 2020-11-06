package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CheckReviewAuthorityException extends RuntimeException {
    public CheckReviewAuthorityException(String message) {
        super(message);
    }
}
