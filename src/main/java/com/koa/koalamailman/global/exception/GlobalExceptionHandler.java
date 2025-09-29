package com.koa.koalamailman.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.koa.koalamailman.global.dto.ErrorResponse;
import com.koa.koalamailman.global.exception.error.AuthErrorCode;
import com.koa.koalamailman.global.exception.error.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Business Exception
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        var errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * HttpMessageNotReadableException
     * JSON parsing, Enum Exception
     */
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

        var errorCode = BaseErrorCode.JSON_PARSE_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * MethodArgumentNotValidException
     * Request body, parameter @valid Exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String messages = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorCode = BaseErrorCode.INVALID_REQUEST;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), String.valueOf(messages)));
    }

    /**
     * ServletRequestBindingException
     * missing header
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleServletRequestBinding(ServletRequestBindingException ex) {
        var errorCode = BaseErrorCode.INVALID_HEADER;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * MissingServletRequestParameterException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        String message = String.format("필수 파라미터 '%s'가 누락되었습니다.", ex.getParameterName());

        var errorCode = BaseErrorCode.INVALID_PARAMETER;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), message));
    }

    /**
     * ConstraintViolationException
     * Request parameter, pathVariable Exception
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    String field = constraintViolation.getPropertyPath().toString();
                    String errorMessage = constraintViolation.getMessage();
                    return field + ":" + errorMessage;
                })
                .collect(Collectors.joining(", "));

        var errorCode = BaseErrorCode.INVALID_PARAMETER;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), message));
    }

    /**
     * AuthenticationException
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex) {
        var errorCode = AuthErrorCode.UNAUTHORIZED;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        var errorCode = AuthErrorCode.FORBIDDEN;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * InsufficientAuthenticationException
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientAuth(InsufficientAuthenticationException ex) {
        var errorCode = AuthErrorCode.UNAUTHORIZED;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), "인증 정보가 부족합니다. 로그인 후 다시 시도해주세요."));
    }

    /**
     * BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        var errorCode = AuthErrorCode.UNAUTHORIZED;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), "아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    /**
     * HttpRequestMethodNotSupportedException
     * HTTP method not supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        var errorCode = BaseErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * NoHandlerFoundException
     * request URL not mapping
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        var errorCode = BaseErrorCode.METHOD_NOT_FOUND;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * HttpMediaTypeNotSupportedException
     * Http media not supported
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        var errorCode = BaseErrorCode.UNSUPPORTED_MEDIA_TYPE;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }

    /**
     * DataIntegrityViolationException
     * DB Constraint
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message;

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();
            message = "DB 제약 조건 위반: " + constraintName;
        } else {
            message = "데이터 무결성 위반 오류가 발생했습니다.";
        }

        var errorCode = BaseErrorCode.DB_CONSTRAINT_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), message));
    }


    /**
     * Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("[Exception] unexpected exception: " + ex);
        var errorCode = BaseErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(new ErrorResponse(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }
}
