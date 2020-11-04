package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessOrderInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@RequiredArgsConstructor
@EnableBinding(PaymentSuccessOrderInputChannel.class)
public class PaymentSuccessStreamListener {

    private final ProductOrderService productOrderService;

    @StreamListener(PaymentSuccessOrderInputChannel.PAYMENT_SUCCESS_ORDER_CONSUMER)
    public void paymentSuccessOrderListener(PaymentRequestDto.Success message) {
        productOrderService.makeOrder(message);
    }

}
