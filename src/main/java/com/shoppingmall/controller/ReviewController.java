package com.shoppingmall.controller;

import com.shoppingmall.service.CartService;
import com.shoppingmall.service.CategoryService;
import com.shoppingmall.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final CartService cartService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;

    @GetMapping("/reviews")
    public String initReview(@RequestParam UUID userId, @RequestParam Long productId, Model model) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("productId", productId);

        model.addAttribute("checkReviewAuthority", cartService.checkReviewAuthority(paramMap));
        model.addAttribute("productNm", reviewService.initReview(productId));
        model.addAttribute("catMapList", categoryService.getCategoryList());

        return "user/review";
    }
}
