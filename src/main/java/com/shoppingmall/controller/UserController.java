package com.shoppingmall.controller;

import com.shoppingmall.service.LoginCompleteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private LoginCompleteService loginCompleteService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "user/login-register";
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

    @GetMapping("/oauth/loginSuccess")
    public String loginComplete(HttpServletRequest request) {
        OAuth2AuthenticationToken authentication
                = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String redirectUrl = null;

        HttpSession session = request.getSession();

        if (session != null) {
            redirectUrl = (String) session.getAttribute("prevPage");
            session.removeAttribute("prevPage");
        }

        if (redirectUrl == null)
            redirectUrl = "/";

        log.info("#### authentication : " + authentication);

        loginCompleteService.loginProc(authentication);

        return "redirect:" + redirectUrl;
    }

    // form 로그아웃, oauth2 로그아웃 공통
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // remember-me 쿠키도 지워야 함
        new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, authentication);

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }

}
