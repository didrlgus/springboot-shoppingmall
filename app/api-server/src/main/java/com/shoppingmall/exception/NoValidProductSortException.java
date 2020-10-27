package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoValidProductSortException extends RuntimeException {
    public NoValidProductSortException(String msg) {
        super(msg);
    }
}
