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
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${app.host}")
    private String host;

    public void sendPaymentSuccessMail(PaymentRequestDto.Success successMessage) {

        Context context = getPaymentSuccessMailContext(successMessage);

        String message = templateEngine.process("mail/payment-success-mail", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(successMessage.getEmail());
            mimeMessageHelper.setSubject("Fancy Cart 결제 내역 입니다.");
            mimeMessageHelper.setText(message, true);
            javaMailSender.send(mimeMessage);
            log.info("메일 전송에 성공했습니다.");
        }
        catch(Exception e){
            log.error("메일 전송에 실패했습니다.", e);
        }
    }

    private Context getPaymentSuccessMailContext(PaymentRequestDto.Success successMessage) {
        Context context = new Context();
        context.setVariable("buyerName", successMessage.getBuyerName());
        context.setVariable("message", successMessage.getOrderName() + " 주문 내역 입니다.");
        context.setVariable("orderNumber", successMessage.getOrderNumber());
        context.setVariable("orderName", successMessage.getOrderName());
        context.setVariable("deliveryMessage", successMessage.getDeliveryMessage().equals("") ? "없음" : successMessage.getDeliveryMessage());
        context.setVariable("address", successMessage.getAddress());
        context.setVariable("amount", successMessage.getAmount() + successMessage.getUseSavings());
        context.setVariable("useSavings", successMessage.getUseSavings());
        context.setVariable("resultAmount", successMessage.getAmount());
        context.setVariable("host", host);

        return context;
    }
}
