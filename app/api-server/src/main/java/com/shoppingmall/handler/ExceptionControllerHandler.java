package com.shoppingmall.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ExceptionControllerHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({Exception.class})
    public ModelAndView defaultExceptionHandler(Exception exception) {

        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("exception", exception.getMessage());

        return mv;
    }
}
