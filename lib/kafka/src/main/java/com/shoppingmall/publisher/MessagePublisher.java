package com.shoppingmall.publisher;

import com.shoppingmall.channel.PaymentSuccessOutputChannel;
import com.shoppingmall.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessagePublisher {

    private final PaymentSuccessOutputChannel paymentSuccessOutputChannel;

    public void publishPaymentSuccessMessage(PaymentRequestDto.Success message) {
        paymentSuccessOutputChannel.outputChannel().send(MessageBuilder.withPayload(message).build());
    }

}
