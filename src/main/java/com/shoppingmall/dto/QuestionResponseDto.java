package com.shoppingmall.dto;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class QuestionResponseDto {

    private Long id;
    private String message;
    private boolean answerState;
    private Integer answerCount;
    private String createdDate;
    private LocalDateTime updatedDate;
    private NormalUser normalUser;
    private Product product;
}
