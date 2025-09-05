package com.koa.koalamailman.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String input;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.input = null;
    }

    public BaseException(ErrorCode errorCode, String input) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.input = input;
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatusCode();
    }
}
