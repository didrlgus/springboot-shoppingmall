package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductDisPrcRequestDto {

    private Long productId;
    @NotNull(message = "시작시간을 작성해주세요.")
    private LocalDateTime startDt;
    @NotNull(message = "종료시간을 작성해주세요.")
    private LocalDateTime endDt;
    @NotNull(message = "할인률을 작성해주세요.")
    private int disPrc;
}
