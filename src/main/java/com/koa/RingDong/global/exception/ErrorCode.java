package com.koa.RingDong.global.exception;

import com.koa.RingDong.domain.user.repository.AgeGroup;
import com.koa.RingDong.domain.user.repository.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 BAD REQUEST
     **/
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터입니다."),

    /**
     * 401 UNAUTHORIZED
     **/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /**
     * 403 FORBIDDEN
     **/
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    /**
     * 404 NOT FOUND
     **/
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    MANDALART_NOT_FOUND(HttpStatus.NOT_FOUND, "만다라트를 찾을 수 없습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "목표를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),

    /**
     * 405 METHOD_NOT_ALLOWED
     **/
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),

    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    GENDER_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, String.format("잘못된 gender입니다. expected: %s", Gender.getNames())),
    AGEGROUP_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, String.format("잘못된 ageGroup입니다. expected: %s", AgeGroup.getNames())),

    DUPLICATE_GOAL_POSITION(HttpStatus.UNPROCESSABLE_ENTITY, "목표 위치가 중복되었습니다."),




    /**
     * 500 INTERNAL SERVER ERROR
     **/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
