package com.shoppingmall.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PaymentSuccessOutputChannel {

    String PAYMENT_SUCCESS_CHANNEL_OUTPUT_NAME = "payment-success-event-to-kafka";

    @Output(PAYMENT_SUCCESS_CHANNEL_OUTPUT_NAME)
    MessageChannel outputChannel();

}
