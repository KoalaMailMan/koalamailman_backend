package com.koa.koalamailman.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.koa.koalamailman.global.dto.ErrorResponse;
import com.koa.koalamailman.global.exception.error.BaseErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        var errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), e.getMessage()));
    }

    // enum 에러 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            Class<?> targetEnum = ife.getTargetType();

            String expectedValues = Arrays.stream(targetEnum.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String message = String.format("잘못된 %s 값입니다. expected: %s",
                    targetEnum.getSimpleName(),
                    expectedValues);

            var errorCode = BaseErrorCode.ENUM_VALUE_INVALID;
            return ResponseEntity
                    .status(errorCode.getHttpStatusCode())
                    .body(new ErrorResponse(errorCode.getHttpStatusCode(), message));
        }

        throw ex;
    }
}
