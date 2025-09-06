package com.koa.koalamailman.global.exception.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MandalartErrorCode implements ErrorCode {

    /**
     * 404 NOT FOUND
     **/
    MANDALART_NOT_FOUND(HttpStatus.NOT_FOUND, "만다라트를 찾을 수 없습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "목표를 찾을 수 없습니다."),

    /**
     * 422 UNPROCESSABLE_ENTITY
     */

    DUPLICATE_GOAL_POSITION(HttpStatus.UNPROCESSABLE_ENTITY, "목표 위치가 중복되었습니다."),
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
