package com.koa.koalamailman.domain.recommend.dto;

public record ChatResponse(
        String response
) {
    public static ChatResponse from(String response) {
        return new ChatResponse(response == null ? "" : response);
    }
}
