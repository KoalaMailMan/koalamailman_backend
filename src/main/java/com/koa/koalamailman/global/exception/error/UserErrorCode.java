package com.koa.koalamailman.global.exception.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserErrorCode implements ErrorCode {


    /**
     * 404 NOT FOUND
     **/
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

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
