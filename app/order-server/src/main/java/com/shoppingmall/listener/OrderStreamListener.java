package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessOrderInputChannel;
import com.shoppingmall.channel.PaymentSuccessSavingsInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * 결제 성공 Event Consumer
 */
@RequiredArgsConstructor
@EnableBinding({PaymentSuccessOrderInputChannel.class, PaymentSuccessSavingsInputChannel.class})
public class OrderStreamListener {

    private final OrderService paymentSuccessService;

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

}
