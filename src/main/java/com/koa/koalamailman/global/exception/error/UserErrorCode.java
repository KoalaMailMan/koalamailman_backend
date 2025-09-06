package com.koa.koalamailman.global.exception.error;

import com.koa.koalamailman.domain.user.repository.AgeGroup;
import com.koa.koalamailman.domain.user.repository.Gender;
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

    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    GENDER_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, String.format("잘못된 gender입니다. expected: %s", Gender.getNames())),
    AGEGROUP_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, String.format("잘못된 ageGroup입니다. expected: %s", AgeGroup.getNames())),

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
