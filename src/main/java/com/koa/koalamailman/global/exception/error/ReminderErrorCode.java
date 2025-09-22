package com.koa.koalamailman.global.exception.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReminderErrorCode implements ErrorCode {


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
