package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class ProductRequestDto {

    @NotBlank(message = "상품명을 작성하세요.")
    @Size(max = 200, message = "상품명을 알맞게 작성해주세요.")
    private String productNm;

    private Integer price;

    @NotBlank(message = "타이틀 이미지 경로를 작성하세요.")
    @Size(max = 200, message = "타이틀 이미지 경로를 알맞게 작성해주세요.")
    private String titleImg;

    @NotBlank(message = "상위 카테고리를 작성하세요.")
    @Size(max = 50, message = "상위 카테고리를 알맞게 작성해주세요.")
    private String largeCatCd;

    @NotBlank(message = "하위 카테고리를 작성하세요.")
    @Size(max = 50, message = "하위 카테고리를 알맞게 작성해주세요.")
    private String smallCatCd;

    private Integer totalCount;
}
