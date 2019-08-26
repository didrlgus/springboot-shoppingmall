package com.shoppingmall.dto;

import com.shoppingmall.domain.Question;
import com.shoppingmall.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ProductResponseDto {

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
    private List<Question> questions;

    @Getter
    @Builder
    @ToString
    public static class MainProductResponseDto {
        private Long id;
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
