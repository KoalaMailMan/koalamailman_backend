package com.koa.koalamailman.global.exception;

import com.koa.koalamailman.global.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
