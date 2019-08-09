package com.shoppingmall.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class ProductController {

    @GetMapping("/productList")
    public String initProductList(Model model) {

        model.addAttribute("pageName", "productList");

        return "product/productList";
    }

    @GetMapping("/productDetails")
    public String initProductDetails() {

        return "product/productDetails";
    }

    @GetMapping("/saleProductList")
    public String initSaleProductList(Model model) {

        model.addAttribute("pageName", "saleProductList");

        return "product/saleProductList";
    }
}
