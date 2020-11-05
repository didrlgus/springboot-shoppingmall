package com.shoppingmall.restcontroller;

import com.shoppingmall.service.ProductOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Api(tags = "productOrder", description = "상품 주문")
@RestController
public class ProductOrderRestController {

    private ProductOrderService productOrderService;

    @ApiOperation(value = "주문 상세")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        return ResponseEntity.ok().body(productOrderService.getOrderDetails(orderId));
    }

    @ApiOperation(value = "전체 주문 조회")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/users/{userId}/orders/{page}")
    public ResponseEntity<?> getAllOrder(@PathVariable("userId") UUID userId, @PathVariable("page") int page) {

        return ResponseEntity.ok().body(productOrderService.getAllOrder(userId, page));
    }
}
