package com.shoppingmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String initMain(ModelMap model) {

        model.addAttribute("pageName", "main");

        return "main/main";
    }
}
