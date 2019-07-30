package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.ProductOrderRequestDto;
import com.shoppingmall.service.ProductOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
public class ProductOrderRestController {

    private ProductOrderService productOrderService;

    @PostMapping("/order")
    public ResponseEntity<?> makeOrder(@RequestBody @Valid ProductOrderRequestDto productOrderRequestDto,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        log.info("productOrder : {}", productOrderRequestDto);

        productOrderService.makeOrder(productOrderRequestDto);

        return ResponseEntity.ok().body("결제가 완료되었습니다");
    }

}
