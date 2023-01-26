package com.yuliavslv.shop.backend.exception;

import com.yuliavslv.shop.backend.dto.AppError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.NoSuchElementException;

@RestControllerAdvice(annotations = RestController.class)
public class AppExceptionHandler {
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NoSuchElementException.class)
    public AppError handleException(NoSuchElementException e) {
        return new AppError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public AppError handleException(DataIntegrityViolationException e) {
        return new AppError(
                HttpStatus.CONFLICT.value(),
                e.getCause().getCause().getMessage()
        );
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalArgumentException.class)
    public AppError handleException(IllegalArgumentException e) {
        return new AppError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage()
        );
    }
}
