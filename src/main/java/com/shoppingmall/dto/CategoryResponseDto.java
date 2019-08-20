package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CategoryResponseDto {

    @Getter
    @Builder
    @ToString
    public static class FirstCategory {
        private Long id;
        private String catNm;
        private Character useYn;
    }

    @Getter
    @Builder
    @ToString
    public static class SecondCategory {
        private Long id;
        private String catNm;
        private String catCd;
        private String upprCatNm;
        private Character useYn;
    }
}
