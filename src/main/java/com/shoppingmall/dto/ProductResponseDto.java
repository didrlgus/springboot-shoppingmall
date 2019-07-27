package com.shoppingmall.dto;

import com.shoppingmall.domain.ProductImg;
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
    private Integer purchaseCount;
    private Integer limitCount;
    private Integer totalCount;
    private ProductStatus productStatus;
    private List<ProductImg> productImg;
    private List<Question> questions;
}
