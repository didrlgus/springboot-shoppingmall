package com.shoppingmall.admin.controller;

import com.shoppingmall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String adminInit(Model model) {
        model.addAttribute("catMapList", categoryService.getAdminCategoryList());

        return "admin/admin-menu-list";
    }

    @GetMapping("/adminProduct")
    public String adminProductInit(Model model) {
        model.addAttribute("catMapList", categoryService.getAdminCategoryList());

        return "admin/admin-product-list";
    }

    @GetMapping("/adminFirstMenu")
    public String registFirstMenuInit() {
        return "admin/first-menu-regist";
    }

    @GetMapping("/adminSecondMenu")
    public String registSecondMenuInit(Model model) {
        model.addAttribute("catMapList", categoryService.getAdminCategoryList());

        return "admin/second-menu-regist";
    }

    @GetMapping("/adminProductRegist")
    public String registProduct(Model model) {
        model.addAttribute("catMapList", categoryService.getAdminCategoryList());

        return "admin/product-regist";
    }

    @GetMapping("/adminProductDetails")
    public String adminProductDetailsInit(Model model) {
        model.addAttribute("catMapList", categoryService.getAdminCategoryList());

        return "admin/admin-product-details";
    }

    @GetMapping("/adminSalePriceList")
    public String adminSalePriceListInit() {
        return "admin/sale-price-list";
    }
}
