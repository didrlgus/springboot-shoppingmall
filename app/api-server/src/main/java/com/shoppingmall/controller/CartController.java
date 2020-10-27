package com.shoppingmall.controller;

import com.shoppingmall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CartController {

    private final CategoryService categoryService;

    @GetMapping("/carts")
    public String initCart(Model model) {

        model.addAttribute("pageName", "cart");
        model.addAttribute("catMapList", categoryService.getCategoryList());

        return "cart/cart";
    }
}
