package com.koa.koalamailman.global.dto;

import com.koa.koalamailman.global.exception.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public static SuccessResponse success(SuccessCode successCode) {
        return new SuccessResponse<>(successCode.getHttpStatusCode(), successCode.getMessage());
    }

    public static <T> SuccessResponse<T> success(SuccessCode successCode, T data) {
        return new SuccessResponse<T>(successCode.getHttpStatusCode(), successCode.getMessage(), data);
    }
}
