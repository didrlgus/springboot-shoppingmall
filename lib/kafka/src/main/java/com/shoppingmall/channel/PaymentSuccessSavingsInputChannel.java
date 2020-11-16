package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessSavingsInputChannel {

    String PAYMENT_SUCCESS_SAVINGS_CONSUMER = "payment-success-savings-consumer";

    @Input(PAYMENT_SUCCESS_SAVINGS_CONSUMER)
    SubscribableChannel inputSavingsChannel();

}
