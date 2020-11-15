package com.shoppingmall.listener;

import com.shoppingmall.channel.PaymentSuccessMailInputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@RequiredArgsConstructor
@EnableBinding(PaymentSuccessMailInputChannel.class)
public class MailStreamListener {

    private final MailService mailService;

    @StreamListener(PaymentSuccessMailInputChannel.PAYMENT_SUCCESS_MAIL_CONSUMER)
    public void paymentSuccessMailListener(PaymentRequestDto.Success message) {
        mailService.sendPaymentSuccessMail(message);
    }

}
