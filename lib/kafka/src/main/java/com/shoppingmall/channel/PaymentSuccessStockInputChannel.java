package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSuccessStockInputChannel {

    String PAYMENT_SUCCESS_STOCK_CONSUMER = "payment-success-stock-consumer";

    @Input(PAYMENT_SUCCESS_STOCK_CONSUMER)
    SubscribableChannel inputStockChannel();

}
