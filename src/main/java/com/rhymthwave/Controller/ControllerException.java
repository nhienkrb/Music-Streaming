package com.rhymthwave.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerException {

	@ExceptionHandler(Exception.class)
    public String handleException() {
        return "redirect:/error/404";
    }
}
