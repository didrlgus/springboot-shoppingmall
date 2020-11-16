package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessMailInputChannel {

    String PAYMENT_SUCCESS_MAIL_CONSUMER = "payment-success-mail-consumer";

    @Input(PAYMENT_SUCCESS_MAIL_CONSUMER)
    SubscribableChannel inputMailChannel();

}
