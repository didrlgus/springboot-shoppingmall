package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.ProductOrderRequestDto;
import com.shoppingmall.dto.ProductOrderResponseDto;
import com.shoppingmall.service.ProductOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        productOrderService.makeOrder(productOrderRequestDto);

        return ResponseEntity.ok().body("결제가 완료되었습니다");
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        return ResponseEntity.ok().body(productOrderService.getOrderDetails(orderId));
    }

    @GetMapping("/user/{userId}/order/{page}")
    public ResponseEntity<?> getAllOrder(@PathVariable("userId") Long userId, @PathVariable("page") int page,
                                         @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(productOrderService.getAllOrder(userId, page, pageable));
    }
}
