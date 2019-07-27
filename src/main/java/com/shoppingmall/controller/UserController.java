package com.shoppingmall.controller;

import com.shoppingmall.dto.NormalUserRequestDto;
import com.shoppingmall.service.SocialLoginCompleteService;
import com.shoppingmall.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private SocialLoginCompleteService socialLoginCompleteService;
    private UserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) throws Exception{
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "user/login-register";
    }

    @PostMapping("/loginFailure")
    public String loginFailure() throws Exception {

        return "user/login-register";
    }

    // 일반유저 회원가입
    @PostMapping("/member")
    public String registration(@ModelAttribute @Valid NormalUserRequestDto userRequestDto)
            throws Exception {

        log.info("### userDto :" + userRequestDto);

        userService.userRegistration(userRequestDto);

        return "user/register-complete";
    }

    // 회원 프로필 화면
    @GetMapping("/profiles")
    public String profiles(Model model) throws Exception {

        model.addAttribute("pageName", "profiles");

        return "user/profiles";
    }

    @GetMapping("/cart")
    public String cart(Model model) throws Exception {

        model.addAttribute("pageName", "cart");

        return "user/cart";
    }

    @GetMapping("/checkout")
    public String checkout() throws Exception {

        return "user/checkout";
    }

    // oauth2 로그인 성공 시 호출
    @GetMapping("/oauth/loginSuccess")
    public String loginComplete(HttpServletRequest request) throws Exception {
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

        socialLoginCompleteService.loginProc(authentication);

        return "redirect:" + redirectUrl;
    }

    // form 로그아웃, oauth2 로그아웃 공통
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // remember-me 쿠키도 지워야 함
        new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, authentication);

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }
}
