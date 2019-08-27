package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.CartRequestDto;
import com.shoppingmall.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "carts", description = "장바구니")
@RestController
public class CartRestController {

    private CartService cartService;

    // 카트 조회
    @ApiOperation(value = "장바구니 조회")
    @GetMapping("/user/{userId}/cart/{page}")
    public ResponseEntity<?> getCartList(@PathVariable("userId") Long userId, @PathVariable("page") int page,
                                         @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(cartService.getCartList(userId, page, pageable));
    }

    // 카트 추가
    @ApiOperation(value = "장바구니 추가")
    @PostMapping("/cart")
    public ResponseEntity<?> makeCart(@RequestBody @Valid CartRequestDto cartRequestDto,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        cartService.makeCart(cartRequestDto);

        return ResponseEntity.ok().body("장바구니에 추가되었습니다.");
    }

    // 카트 삭제
    @ApiOperation(value = "장바구니 삭제")
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<?> removeCart(@PathVariable Long id) {

        cartService.removeCart(id);

        return ResponseEntity.ok().body("삭제 되었습니다.");
    }
}
