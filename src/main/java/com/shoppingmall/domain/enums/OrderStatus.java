package com.shoppingmall.domain.enums;

public enum OrderStatus {
    COMPLETE("결제완료"), CANCEL("결제취소"), REFUND("환불"), WAIT("배송중");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}
