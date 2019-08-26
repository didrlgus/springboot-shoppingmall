package com.shoppingmall.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
