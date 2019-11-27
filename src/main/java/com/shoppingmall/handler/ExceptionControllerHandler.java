package com.shoppingmall.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ExceptionControllerHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("exception", exception.getMessage());

        return mv;
    }
}
