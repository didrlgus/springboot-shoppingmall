package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.publisher.MessagePublisher;
import com.shoppingmall.channel.PaymentSuccessOutputChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "payment", description = "상품결제")
@RequiredArgsConstructor
@EnableBinding(PaymentSuccessOutputChannel.class)
@RestController
public class PaymentRestController {

    private final MessagePublisher messagePublisher;

    @ApiOperation(value = "결제 성공")
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
