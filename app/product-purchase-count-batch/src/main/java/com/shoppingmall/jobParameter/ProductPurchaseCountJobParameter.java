package com.shoppingmall.jobParameter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
public class ProductPurchaseCountJobParameter {

    private LocalDateTime dateTime;

    public ProductPurchaseCountJobParameter(String requestDateTimeStr) {
        this.dateTime = LocalDateTime.parse(requestDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
