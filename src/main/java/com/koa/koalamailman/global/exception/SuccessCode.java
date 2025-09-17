package com.koa.koalamailman.global.exception;

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
    GET_ACCESS_TOKEN(HttpStatus.OK, "유저 access token 조회 성공입니다."),
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공입니다."),
    UPDATE_USER_PROFILE_SUCCESS(HttpStatus.OK, "유저 프로필 업데이트 성공입니다."),

    CREATE_MANDALART_SUCCESS(HttpStatus.OK, "만다라트 생성 성공입니다."),

    GET_MANDALART_SUCCESS(HttpStatus.OK, "만다라트 조회 성공입니다."),
    UPDATE_MANDALART_SUCCESS(HttpStatus.OK, "만다라트 수정 성공입니다."),
    SEND_MAIL_SUCCESS(HttpStatus.OK, "메일이 전송되었습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
