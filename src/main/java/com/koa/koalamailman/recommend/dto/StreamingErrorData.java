package com.koa.koalamailman.recommend.dto;

import com.koa.koalamailman.global.exception.error.ErrorCode;

public record StreamingErrorData(
        String code,
        String message
) {
    public static StreamingErrorData from(ErrorCode errorCode) {
        return new StreamingErrorData(errorCode.getCode(), errorCode.getMessage());
    }
}