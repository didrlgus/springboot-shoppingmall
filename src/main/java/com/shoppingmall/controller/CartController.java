package com.shoppingmall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CartController {

    @GetMapping("/carts")
    public String initCart(Model model) {

        model.addAttribute("pageName", "cart");

        return "cart/cart";
    }
}
