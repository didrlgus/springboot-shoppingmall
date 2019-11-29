package com.shoppingmall.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    // 로그인 성공 시 호출되는 메소드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 관리자로 로그인할 경우
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            String redirectUrl = "/admin";

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

            return;
        }

        // 일반 유저로 로그인할 경우
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");

            if (!redirectUrl.equals("/login")) {
                session.removeAttribute("prevPage");
                //session.setAttribute("user", authentication);
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                getRedirectStrategy().sendRedirect(request, response, "/");
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}