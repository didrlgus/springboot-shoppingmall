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

}
