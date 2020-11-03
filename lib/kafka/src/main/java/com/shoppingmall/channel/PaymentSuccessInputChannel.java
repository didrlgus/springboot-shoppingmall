package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessInputChannel {

    String PAYMENT_SUCCESS_CHANNEL_INPUT_NAME = "payment-success-event-from-kafka";

    @Input(PAYMENT_SUCCESS_CHANNEL_INPUT_NAME)
    SubscribableChannel inputChannel();

}
