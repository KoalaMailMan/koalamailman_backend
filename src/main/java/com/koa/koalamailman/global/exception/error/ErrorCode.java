package com.koa.koalamailman.global.exception.error;

public interface ErrorCode {
    int getHttpStatusCode();
    String getMessage();

    default String getCode() {
        return ((Enum<?>) this).name();
    }
}
