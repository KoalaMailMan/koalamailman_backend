package com.koa.koalamailman.global.exception.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.net.URI;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendErrorCodeTest {

    @Test
    @DisplayName("TimeoutException이 발생하면 RECOMMEND_TIMEOUT을 반환한다")
    void fromException_timeout() {
        // given
        Throwable exception = new TimeoutException("timeout");

        // when
        RecommendErrorCode errorCode = RecommendErrorCode.fromException(exception);

        // then
        assertThat(errorCode).isEqualTo(RecommendErrorCode.RECOMMEND_TIMEOUT);
    }

    @Test
    @DisplayName("WebClientRequestException이 발생하면 RECOMMEND_LLM_CONNECTION_ERROR를 반환한다")
    void fromException_connectionError() {
        // given
        Throwable exception = new WebClientRequestException(
                new RuntimeException("connection failed"),
                org.springframework.http.HttpMethod.POST,
                URI.create("http://localhost"),
                org.springframework.http.HttpHeaders.EMPTY
        );

        // when
        RecommendErrorCode errorCode = RecommendErrorCode.fromException(exception);

        // then
        assertThat(errorCode).isEqualTo(RecommendErrorCode.RECOMMEND_LLM_CONNECTION_ERROR);
    }

    @Test
    @DisplayName("알 수 없는 예외가 발생하면 RECOMMEND_STREAM_ERROR를 반환한다")
    void fromException_unknownError() {
        // given
        Throwable exception = new RuntimeException("unknown error");

        // when
        RecommendErrorCode errorCode = RecommendErrorCode.fromException(exception);

        // then
        assertThat(errorCode).isEqualTo(RecommendErrorCode.RECOMMEND_STREAM_ERROR);
    }
}
