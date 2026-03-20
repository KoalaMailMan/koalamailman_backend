package com.koa.koalamailman.global.exception.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.concurrent.TimeoutException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RecommendErrorCode implements ErrorCode {

    RECOMMEND_NOT_CONTENT(HttpStatus.NO_CONTENT, "목표 추천 내용이 없습니다."),
    RECOMMEND_LLM_CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "LLM 서비스에 연결할 수 없습니다."),
    RECOMMEND_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "LLM 응답 시간이 초과되었습니다."),
    RECOMMEND_STREAM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "스트리밍 처리 중 오류가 발생했습니다."),
    RECOMMEND_ALL_MODELS_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "모든 LLM 모델 호출에 실패했습니다."),
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

    public static RecommendErrorCode fromException(Throwable e) {
        if (e instanceof TimeoutException) {
            return RECOMMEND_TIMEOUT;
        }
        if (e instanceof WebClientRequestException) {
            return RECOMMEND_LLM_CONNECTION_ERROR;
        }
        return RECOMMEND_STREAM_ERROR;
    }
}
