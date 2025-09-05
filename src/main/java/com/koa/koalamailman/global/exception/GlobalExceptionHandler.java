package com.koa.koalamailman.global.exception;

import com.koa.koalamailman.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        var errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), e.getMessage()));
    }
}
