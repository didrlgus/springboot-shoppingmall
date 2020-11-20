package com.shoppingmall.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductCount {

    private Long productId;
    private Integer count;

}
