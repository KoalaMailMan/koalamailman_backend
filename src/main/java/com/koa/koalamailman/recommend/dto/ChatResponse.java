package com.koa.koalamailman.recommend.dto;

public record ChatResponse(
        String response
) {
    public static ChatResponse from(String response) {
        return new ChatResponse(response == null ? "" : response);
    }
}
