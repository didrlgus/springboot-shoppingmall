package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.publisher.MessagePublisher;
import com.shoppingmall.publisher.PaymentSuccessChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@EnableBinding(PaymentSuccessChannel.class)
@RestController
public class PaymentRestController {

    private final MessagePublisher messagePublisher;

    @PostMapping("/payment/success")
    public ResponseEntity<?> paymentSuccess(@RequestBody @Valid PaymentRequestDto.Success requestDto,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        messagePublisher.publishPaymentSuccessMessage(requestDto);

        return ResponseEntity.ok("결제가 완료되었습니다.");
    }

}
