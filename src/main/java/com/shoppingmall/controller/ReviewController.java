package com.shoppingmall.controller;

import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@AllArgsConstructor
@Controller
public class ReviewController {

    private ProductService productService;
    private CartService cartService;

    @GetMapping("/reviews")
    public String initReview(@RequestParam Long userId, @RequestParam Long productId, Model model) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("productId", productId);

        model.addAttribute("checkReviewAuthority", cartService.checkReviewAuthority(paramMap));
        model.addAttribute("productNm", productService.initReview(productId));

        return "user/review";
    }
}
