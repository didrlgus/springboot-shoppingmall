package com.shoppingmall.domain.enums;

public enum ProductStatus {

    SOLDOUT("품절"),
    TEMPSOLDOUT("일시품절"),
    SALE("판매중");

    private String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}
