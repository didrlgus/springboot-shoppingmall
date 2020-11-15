package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessCountInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.ProductPurchaseCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@RequiredArgsConstructor
@EnableBinding({PaymentSuccessCountInputChannel.class})
public class ProductPurchaseCountStreamListener {

    private final ProductPurchaseCountService productPurchaseCountService;

    /**
     상품 수량 업데이트 Consumer
     */
    @StreamListener(PaymentSuccessCountInputChannel.PAYMENT_SUCCESS_COUNT_CONSUMER)
    public void paymentSuccessProductCountListener(PaymentRequestDto.Success message) {
        productPurchaseCountService.updateProductPurchaseCount(message);
    }

}
