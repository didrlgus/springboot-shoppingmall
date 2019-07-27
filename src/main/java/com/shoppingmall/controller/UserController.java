package com.shoppingmall.controller;

import com.shoppingmall.service.NormalUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private NormalUserService normalUserService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "user/login-register";
    }

    @GetMapping("/registration")
    public void registration() {

        normalUserService.registration();

    }

    @GetMapping("/profiles")
    public String profiles() {

        return "user/profiles";
    }

    @GetMapping("/cart")
    public String cart() {

        return "user/cart";
    }

    @GetMapping("/checkout")
    public String checkout() {

        return "user/checkout";
    }
}
