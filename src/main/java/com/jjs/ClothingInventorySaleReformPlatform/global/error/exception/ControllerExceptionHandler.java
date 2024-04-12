package com.jjs.ClothingInventorySaleReformPlatform.global.error.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public Map<String, String> validationException(CustomValidationException e) {
        return e.getErrorMap();
    }


}
