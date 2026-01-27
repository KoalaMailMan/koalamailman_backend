package com.koa.koalamailman.domain.recommend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.koa.koalamailman.global.exception.error.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StreamingMessage(
        String type,
        String content,
        String code,
        String message
) {
    public static StreamingMessage data(String content) {
        return new StreamingMessage("data", content, null, null);
    }

    public static StreamingMessage complete() {
        return new StreamingMessage("complete", null, null, null);
    }

    public static StreamingMessage error(ErrorCode errorCode) {
        return new StreamingMessage("error", null, errorCode.getCode(), errorCode.getMessage());
    }

    public static StreamingMessage error(String code, String message) {
        return new StreamingMessage("error", null, code, message);
    }
}
