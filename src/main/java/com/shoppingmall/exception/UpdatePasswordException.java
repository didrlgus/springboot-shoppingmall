package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UpdatePasswordException extends RuntimeException{

    public UpdatePasswordException(String msg) {
        super(msg);
    }
}
