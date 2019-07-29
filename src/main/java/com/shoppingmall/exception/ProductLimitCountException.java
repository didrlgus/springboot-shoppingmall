package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProductLimitCountException extends RuntimeException {

    public ProductLimitCountException(String msg) {
        super(msg);
    }
}
