package com.swire.etsww.config;

import com.swire.etsww.base.ApiResonpse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResonpse handle(MethodArgumentNotValidException exception) {
        return ApiResonpse.badRequest(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResonpse handle(ConstraintViolationException exception) {
        return ApiResonpse.badRequest(exception.getMessage());
    }
}