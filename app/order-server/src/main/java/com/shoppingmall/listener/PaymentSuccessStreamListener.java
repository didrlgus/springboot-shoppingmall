package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessOrderInputChannel;
import com.shoppingmall.channel.PaymentSuccessSavingsInputChannel;
import com.shoppingmall.channel.PaymentSuccessCountInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.PaymentSuccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * 결제 성공 Event Consumer
 */
@RequiredArgsConstructor
@EnableBinding({PaymentSuccessOrderInputChannel.class, PaymentSuccessSavingsInputChannel.class,
        PaymentSuccessCountInputChannel.class})
public class PaymentSuccessStreamListener {

    private final PaymentSuccessService paymentSuccessService;

    /**
     주문서 생성 Consumer
     **/
    @StreamListener(PaymentSuccessOrderInputChannel.PAYMENT_SUCCESS_ORDER_CONSUMER)
    public void paymentSuccessOrderListener(PaymentRequestDto.Success message) {
        paymentSuccessService.makeOrder(message);
    }

    /**
     적립금 업데이트 Consumer
     **/
    @StreamListener(PaymentSuccessSavingsInputChannel.PAYMENT_SUCCESS_SAVINGS_CONSUMER)
    public void paymentSuccessSavingsListener(PaymentRequestDto.Success message) {
        paymentSuccessService.updateSavings(message);
    }

    /**
     상품 수량 업데이트 Consumer
     */
    @StreamListener(PaymentSuccessCountInputChannel.PAYMENT_SUCCESS_COUNT_CONSUMER)
    public void paymentSuccessProductCountListener(PaymentRequestDto.Success message) {
        paymentSuccessService.updateProductPurchaseCount(message);
    }
}
