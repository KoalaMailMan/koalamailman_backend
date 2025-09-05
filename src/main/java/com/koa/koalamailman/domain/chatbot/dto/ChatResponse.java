package com.koa.koalamailman.domain.chatbot.dto;

public record ChatResponse(
        String response
) {
    public static ChatResponse from(String response) {
        return new ChatResponse(response == null ? "" : response);
    }
}
