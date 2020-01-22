package com.tzxx.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tzxx
 */
@ControllerAdvice(basePackages = "com.tzxx")
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult<Object> jsonErrorHandler(Exception ex){
        JsonResult<Object> value =  new JsonResult<>(-1, ex.getMessage());
        logger.error("global_exception_handler_print_error:{0}", ex);
        return value;
    }

}