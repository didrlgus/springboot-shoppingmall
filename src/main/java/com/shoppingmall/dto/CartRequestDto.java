package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class CartRequestDto {

    private Long userId;
    private Long productId;
    @Min(value = 1, message = "수량은 1보다 작을 수 없습니다.")
    private Integer productCount;
}
