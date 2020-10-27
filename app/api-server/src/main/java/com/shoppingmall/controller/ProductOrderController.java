package com.shoppingmall.controller;

import com.shoppingmall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductOrderController {

    private final CategoryService categoryService;

    @GetMapping("/orders")
    public String initProductOrder(Model model) {

        model.addAttribute("pageName", "order");
        model.addAttribute("catMapList", categoryService.getCategoryList());

        return "user/order";
    }
}
