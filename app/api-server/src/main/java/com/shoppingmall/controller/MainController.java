package com.shoppingmall.controller;

import com.shoppingmall.service.CategoryService;
import com.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String initMain(ModelMap model) {

        model.addAttribute("pageName", "main");
        model.addAttribute("catMapList", categoryService.getCategoryList());
        model.addAttribute("bestProductList", productService.getBestProductList());
        model.addAttribute("newProductList", productService.getNewProductList());

        return "main/main";
    }
}
