package com.shoppingmall.restcontroller;

import com.shoppingmall.domain.UploadFile;
import com.shoppingmall.dto.ReviewRequestDto;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@AllArgsConstructor
@RestController
public class ReviewRestController {

    private ReviewService reviewService;
    private CartService cartService;

    @PostMapping("/review/image")
    public ResponseEntity<?> uploadReviewImage(@RequestParam("file") MultipartFile file) {

        try {
            UploadFile uploadedFile = reviewService.uploadReviewImage(file);
            return ResponseEntity.ok().body("review-upload-image/" + uploadedFile.getSaveFileName());

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 리뷰를 작성할 수 있는 회원인지 파악 (해당 상품을 결제한 유저는 리뷰작성 가능)
    @GetMapping("/review/authority")
    public ResponseEntity<?> checkReviewAuthority(@RequestParam HashMap<String, Object> paramMap) {

        return ResponseEntity.ok().body(cartService.checkReviewAuthority(paramMap));
    }

    // 리뷰 추가
    @PostMapping("/review")
    public ResponseEntity<?> makeReview(@RequestBody @Valid ReviewRequestDto reviewRequestDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        reviewService.makeReview(reviewRequestDto);

        return ResponseEntity.ok().body("리뷰가 추가되었습니다.");
    }

    // 리뷰리스트 조회
    @GetMapping("/product/{productId}/review/{page}")
    public ResponseEntity<?> getReviewList(@PathVariable("productId") Long productId,
                                           @PathVariable("page") int page) {

        return ResponseEntity.ok().body(reviewService.getReviewList(productId, page));
    }
}
