package com.shoppingmall.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminInit() {
        return "admin/admin-menu-list";
    }

    @GetMapping("/adminProduct")
    public String adminProductInit() {
        return "admin/admin-product-list";
    }

    @GetMapping("/adminFirstMenu")
    public String registFirstMenuInit() {
        return "admin/first-menu-regist";
    }

    @GetMapping("/adminSecondMenu")
    public String registSecondMenuInit() { return "admin/second-menu-regist"; }

    @GetMapping("/adminProductRegist")
    public String registProduct() { return "admin/product-regist"; }

    @GetMapping("/adminProductDetails")
    public String adminProductDetailsInit() {
        return "admin/admin-product-details";
    }

    @GetMapping("/adminSalePriceList")
    public String adminSalePriceListInit() {
        return "admin/sale-price-list";
    }
}
