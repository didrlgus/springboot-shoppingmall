package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
public class UserResponseDto {

    private UUID id;
    private String identifier;
    private String email;
    private String name;
    private String roadAddr;
    private String buildingName;
    private String detailAddr;
    private Integer savings;

    @Setter
    @Getter
    @Builder
    @ToString
    public static class ReviewUserResponseDto {
        private String identifier;
    }

    @Getter
    @Builder
    public static class QuestionUserResponseDto {
        private UUID id;
        private String identifier;
    }

}
