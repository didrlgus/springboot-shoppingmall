package com.shoppingmall.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("password", request.getParameter("password"));

        // 비밀번호 틀릴 경우
        if (exception.getMessage().equals("Bad credentials"))
            request.setAttribute("errorMsg", "로그인에 실패하였습니다!");
        else    // 아이디 틀릴 경우 or  이미 탈퇴된 유저일 경우
            request.setAttribute("errorMsg", exception.getMessage());

        request.getRequestDispatcher("/loginFailure").forward(request, response);
    }
}
