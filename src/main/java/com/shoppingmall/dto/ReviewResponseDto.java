package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class ReviewResponseDto {

    private Long id;
    private UserResponseDto.ReviewUserResponseDto userIdentifier;
    private String title;
    private int rate;
    private String createdDate;

    @Getter
    @Builder
    @ToString
    public static class ReviewDetailResponseDto {
        private String content;
    }
}
