package com.koa.koalamailman.global.exception.error;

public interface ErrorCode {
    int getHttpStatusCode();
    String getMessage();
}
