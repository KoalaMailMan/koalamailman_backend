package com.koa.koalamailman.global.dto;

public record ErrorResponse(
        int code,
        String message
) {}
