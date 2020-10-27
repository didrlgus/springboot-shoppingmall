package com.shoppingmall.controller;

import com.shoppingmall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final CategoryService categoryService;

    @GetMapping("/productList")
    public String initProductList(Model model) {

        model.addAttribute("pageName", "productList");
        model.addAttribute("catMapList", categoryService.getCategoryList());

        return "product/productList";
    }

    @GetMapping("/productDetails")
    public String initProductDetails(Model model, @RequestParam("productId") Long id) {

        model.addAttribute("catMapList", categoryService.getCategoryList());
        model.addAttribute("productId", id);

        return "product/productDetails";
    }

    @GetMapping("/saleProductList")
    public String initSaleProductList(Model model) {

        model.addAttribute("pageName", "saleProductList");
        model.addAttribute("catMapList", categoryService.getCategoryList());

        return "product/saleProductList";
    }
}
