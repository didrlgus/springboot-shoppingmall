package com.shoppingmall.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PaymentSuccessChannel {

    @Output("paymentSuccessChannel")
    MessageChannel paymentSuccessChannel();

}
