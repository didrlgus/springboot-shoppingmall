package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.ProductDisPrcRequestDto;
import com.shoppingmall.service.ProductDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Api(tags = "productDiscount", description = "상품 할인")
@RestController
public class ProductDiscountRestController {

    private final ProductDiscountService productDiscountService;

    // 상품 할인 리스트 조회 (관리자 권한)
    @ApiOperation(value = "상품 할인 조회 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products/{id}/discounts")
    public ResponseEntity<?> getProductDiscountList(@PathVariable Long id) {

        return ResponseEntity.ok().body(productDiscountService.getDiscountList(id));
    }

    // 상품 할인 리스트 추가 (관리자 권한)
    @ApiOperation(value = "상품 할인 추가 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/discounts")
    public ResponseEntity<?> addProductDiscount(@RequestBody @Valid ProductDisPrcRequestDto productDisPrcRequestDto,
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(productDiscountService.addProductDiscount(productDisPrcRequestDto));
    }

    // 상품 할인 리스트 삭제 (관리자 권한)
    @ApiOperation(value = "상품 할인 삭제 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/discounts/{id}")
    public ResponseEntity<?> deleteProductDiscount(@PathVariable Long id) {

        return ResponseEntity.ok().body(productDiscountService.deleteProductDiscount(id));
    }
}
