package com.koa.RingDong.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {
    /**
     * 200 SUCCESS
     */
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공입니다."),
    UPDATE_USER_PROFILE_SUCCESS(HttpStatus.OK, "유저 프로필 업데이트 성공입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
