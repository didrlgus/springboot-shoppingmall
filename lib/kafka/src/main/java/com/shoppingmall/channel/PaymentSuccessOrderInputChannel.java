package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessOrderInputChannel {

    String PAYMENT_SUCCESS_ORDER_CONSUMER = "payment-success-order-consumer";

    @Input(PAYMENT_SUCCESS_ORDER_CONSUMER)
    SubscribableChannel inputChannel();

}
