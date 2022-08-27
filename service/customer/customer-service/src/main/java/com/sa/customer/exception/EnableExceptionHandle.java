package com.sa.customer.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//TODO----------------------------------------------------
//@ControllerAdvice
public class EnableExceptionHandle{
    @ExceptionHandler(value = { ClassNotFoundException.class })
    public String handleClassNotFoundException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return "ClassNotFoundException!!!!!!!!!!!";
    }
}
