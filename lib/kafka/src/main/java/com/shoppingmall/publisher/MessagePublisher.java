package com.shoppingmall.publisher;

import com.shoppingmall.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessagePublisher {

    private final PaymentSuccessChannel paymentSuccessChannel;

    public void publishPaymentSuccessMessage(PaymentRequestDto.Success message) {
        paymentSuccessChannel.paymentSuccessChannel().send(MessageBuilder.withPayload(message).build());
    }

}
