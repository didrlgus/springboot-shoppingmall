package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@ToString
public class ProductOrderRequestDto {

    private List<Long> cartIdList;
    @NotBlank(message = "주문번호가 없을 수 없습니다.")
    private String orderNumber;
    @NotBlank(message = "주문명이 없을 수 없습니다.")
    private String orderName;
    private String deliveryMessage;
    @NotBlank(message = "주소를 작성해 주세요.")
    private String address;
    private Integer amount;
    private Integer useSavings;
    private String impUid;
}
