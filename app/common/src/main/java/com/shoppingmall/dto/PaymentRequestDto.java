package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PaymentRequestDto {

    @Getter
    @Builder
    public static class Success {
        private UUID userId;
        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "이메일의 양식을 지켜주세요.")
        private String email;
        @NotBlank(message = "구매자 이름을 입력하세요.")
        @Size(min = 1, max = 10, message = "이름을 알맞게 작성해주세요.")
        private String buyerName;
        @NotBlank(message = "주문번호를 입력하세요.")
        @Size(max = 20, message = "주문번호를 알맞게 작성해주세요.")
        private String orderNumber;
        @NotBlank(message = "주문명을 입력하세요.")
        private String orderName;
        private String deliveryMessage;
        @NotBlank(message = "주소를 입력하세요.")
        @Size(max = 200, message = "도로명 주소를 알맞게 작성해주세요.")
        private String address;
        private Integer amount;
        private Integer useSavings;
        private List<Long> cartIdList;
    }

}
