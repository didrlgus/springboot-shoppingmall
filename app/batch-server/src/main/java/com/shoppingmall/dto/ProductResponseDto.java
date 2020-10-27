package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
public class ProductResponseDto implements Serializable {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MainProductResponseDto implements Serializable {
        private Long id;
        private String productNm;
        private String titleImg;
        private Integer price;
        private Integer disPrice;
        private Integer salePrice;
        private Integer rateAvg;
        private Long timestamp;
        private Integer purchaseCnt;
    }
}
