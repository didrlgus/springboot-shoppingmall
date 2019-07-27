package com.shoppingmall.dto;

import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.SocialUser;
import com.shoppingmall.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class SocialQuestionResponseDto {

    private Long id;
    private SocialType socialType;
    private String message;
    private boolean answerState;
    private Integer answerCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private SocialUser socialUser;
    private Product product;
}
