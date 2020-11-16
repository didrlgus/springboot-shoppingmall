package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistQuestionException extends RuntimeException {

    public NotExistQuestionException(String msg) {
        super(msg);
    }
}
