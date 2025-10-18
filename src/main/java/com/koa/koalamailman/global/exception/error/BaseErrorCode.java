package com.koa.koalamailman.global.exception.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseErrorCode implements ErrorCode {

    /**
     * 400 BAD REQUEST
     **/
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "유효하지 않은 헤더입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터입니다."),

    DB_CONSTRAINT_ERROR(HttpStatus.BAD_REQUEST, "DB 제약 조건 위반 오류가 발생했습니다."),

    METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 HTTP URL입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 매서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 Media type입니다."),


    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    ENUM_VALUE_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "잘못된 Enum 값입니다."),
    JSON_PARSE_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "잘못된 JSON 형식입니다."),


    /**
     * 500 INTERNAL SERVER ERROR
     **/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),

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
