package com.shoppingmall.dto;

import com.shoppingmall.domain.User;
import com.shoppingmall.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CartResponseDto {

    private Long id;
    private User user;
    private Product product;
    private Integer salePrice;
    private Integer productCount;
}
