package com.shoppingmall.dto;

import com.shoppingmall.domain.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class ProductOrderResponseDto {

    private Long id;
    private String orderNumber;
    private String orderName;
    private String deliveryMessage;
    private String orderStatus;
    private String address;
    private Character refundState;
    private Integer amount;
    private String createdDate;
    private List<Cart> carts;
}
