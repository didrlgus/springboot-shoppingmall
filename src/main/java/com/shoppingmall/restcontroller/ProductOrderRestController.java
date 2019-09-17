package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.ProductOrderRequestDto;
import com.shoppingmall.dto.ProductOrderResponseDto;
import com.shoppingmall.service.ProductOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Api(tags = "productOrder", description = "상품 주문")
@RestController
public class ProductOrderRestController {

    private ProductOrderService productOrderService;

    @ApiOperation(value = "주문 생성")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/orders")
    public ResponseEntity<?> makeOrder(@RequestBody @Valid ProductOrderRequestDto productOrderRequestDto,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        productOrderService.makeOrder(productOrderRequestDto);

        return ResponseEntity.ok().body("결제가 완료되었습니다");
    }

    @ApiOperation(value = "주문 상세")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        return ResponseEntity.ok().body(productOrderService.getOrderDetails(orderId));
    }

    @ApiOperation(value = "전체 주문 조회")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/users/{userId}/orders/{page}")
    public ResponseEntity<?> getAllOrder(@PathVariable("userId") Long userId, @PathVariable("page") int page,
                                         @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(productOrderService.getAllOrder(userId, page, pageable));
    }
}
