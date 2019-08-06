package com.shoppingmall.dto;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CartResponseDto {

    private Long id;
    private NormalUser normalUser;
    private Product product;
    private Integer salePrice;
    private Integer productCount;
}
