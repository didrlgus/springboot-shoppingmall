package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class CategoryRequestDto {

    @NotBlank(message = "카테고리명을 적어주세요.")
    @Size(max = 50, message = "카테고리명을 알맞게 적어주세요.")
    private String catNm;
    private Character useYn;

    @Setter
    @Getter
    @ToString
    public static class firstCategory {
        @NotBlank(message = "카테고리명을 적어주세요.")
        @Size(max = 50, message = "카테고리명을 알맞게 적어주세요.")
        private String catNm;
        private Character useYn;
    }

    @Setter
    @Getter
    @ToString
    public static class secondCategory {
        @NotBlank(message = "카테고리명을 적어주세요.")
        @Size(max = 50, message = "카테고리명을 알맞게 적어주세요.")
        private String catNm;
        @NotBlank(message = "상위 카테고리 코드를 적어주세요.")
        @Size(max = 10, message = "상위 카테고리 코드를 알맞게 적어주세요.")
        private String upprCatCd;
        private Character useYn;
    }
}
