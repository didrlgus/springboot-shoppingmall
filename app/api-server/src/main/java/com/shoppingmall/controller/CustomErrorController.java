package com.shoppingmall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        /*Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        log.info("httpStatus : "+httpStatus.toString());*/

        return "error/404";
    }
}
