package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@RequiredArgsConstructor
@EnableBinding(PaymentSuccessInputChannel.class)
public class CustomStreamListener {

    private final MailService mailService;

    @StreamListener(PaymentSuccessInputChannel.PAYMENT_SUCCESS_CHANNEL_INPUT_NAME)
    public void paymentSuccessMailListener(PaymentRequestDto.Success message) {
        mailService.sendPaymentSuccessMail(message);
    }

}
