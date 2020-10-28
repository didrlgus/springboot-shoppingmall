package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ProductStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto implements Serializable {

    private Long id;
    private String productNm;
    private String titleImg;
    private String largeCatCd;
    private String smallCatCd;
    private Integer price;
    private Integer disPrice;
    private Integer purchaseCount;
    private Integer limitCount;
    private Integer totalCount;
    private ProductStatus productStatus;
    private Integer rateAvg;

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

    @Getter
    @Builder
    @ToString
    public static class SaleProductResponseDto {
        private Long productId;
        private String productNm;
        private String titleImg;
        private Integer price;
        private Integer disPrice;
        private Integer salePrice;
        private Integer rateAvg;
    }

    @Getter
    @Builder
    @ToString
    public static class AdminProductResponseDto {
        private Long id;
        private String productNm;
        private String titleImg;
        private Integer price;
        private Integer disPrice;
        private Integer purchaseCount;
        private Integer totalCount;
        private Integer rateAvg;
    }

    @Getter
    @Builder
    @ToString
    public static class AdminProductDetailResponseDto {
        private Long id;
        private String productNm;
        private String titleImg;
        private Integer price;
        private Integer disPrice;
        private String disStartDt;
        private String disEndDt;
        private String largeCatCd;
        private String smallCatCd;
        private Integer totalCount;
    }
}
