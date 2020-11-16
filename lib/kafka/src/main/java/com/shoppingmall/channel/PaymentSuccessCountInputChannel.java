package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessCountInputChannel {

    String PAYMENT_SUCCESS_COUNT_CONSUMER = "payment-success-count-consumer";

    @Input(PAYMENT_SUCCESS_COUNT_CONSUMER)
    SubscribableChannel inputCountChannel();

}
