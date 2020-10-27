package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeleteUserException extends RuntimeException {

    public DeleteUserException(String msg) {
        super(msg);
    }
}
