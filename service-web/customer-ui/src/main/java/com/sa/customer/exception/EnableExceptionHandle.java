package com.sa.customer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//TODO----------------------------------------------------

/**
 * @author xujin
 */
@ControllerAdvice(annotations = {RestController.class})
@Slf4j
public class EnableExceptionHandle{
    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleClassNotFoundException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage());
    }
}
