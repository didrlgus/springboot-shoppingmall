package com.shoppingmall.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class ProductOrderController {

    @GetMapping("/orders")
    public String initProductOrder(Model model) {

        model.addAttribute("pageName", "order");

        return "user/order";
    }
}
