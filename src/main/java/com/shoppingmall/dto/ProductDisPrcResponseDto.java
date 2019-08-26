package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProductDisPrcResponseDto {

    private Long id;
    private Long productId;
    private String startDt;
    private String endDt;
    private int disPrc;
}
