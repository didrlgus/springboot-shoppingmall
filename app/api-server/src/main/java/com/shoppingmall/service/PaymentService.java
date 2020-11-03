package com.shoppingmall.service;

import com.shoppingmall.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${app.host}")
    private String host;

    public void sendEmail(PaymentRequestDto.Success requestDto) {
        Context context = new Context();
        context.setVariable("buyerName", requestDto.getBuyerName());
        context.setVariable("message", requestDto.getOrderName() + " 주문 내역 입니다.");
        context.setVariable("orderNumber", requestDto.getOrderNumber());
        context.setVariable("orderName", requestDto.getOrderName());
        context.setVariable("deliveryMessage", requestDto.getDeliveryMessage().equals("") ? "없음" : requestDto.getDeliveryMessage());
        context.setVariable("address", requestDto.getAddress());
        context.setVariable("amount", requestDto.getAmount());
        context.setVariable("useSavings", requestDto.getUseSavings());
        context.setVariable("resultAmount", requestDto.getAmount() - requestDto.getUseSavings());
        context.setVariable("host", host);

        String message = templateEngine.process("mail/payment-success-mail", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(requestDto.getEmail());
            mimeMessageHelper.setSubject("Fancy Cart 결제 내역 입니다.");
            mimeMessageHelper.setText(message, true);
            javaMailSender.send(mimeMessage);
            log.info("메일 전송에 성공했습니다.");
        }
        catch(Exception e){
            log.error("메일 전송에 실패했습니다.", e);
        }
    }
}
