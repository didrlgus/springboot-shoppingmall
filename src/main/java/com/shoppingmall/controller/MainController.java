package com.shoppingmall.controller;

import com.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class MainController {

    private ProductService productService;

    @GetMapping("/")
    public String initMain(ModelMap model) {

        model.addAttribute("pageName", "main");
        model.addAttribute("bestProductList", productService.getBestProductList());
        model.addAttribute("newProductList", productService.getNewProductList());

        return "main/main";
    }
}
