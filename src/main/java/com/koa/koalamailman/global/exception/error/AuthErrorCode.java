package com.koa.koalamailman.global.exception.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    /**
     * 401 UNAUTHORIZED
     **/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /**
     * 403 FORBIDDEN
     **/
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;
    @Override
    public int getHttpStatusCode() {
        return this.status.value();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
