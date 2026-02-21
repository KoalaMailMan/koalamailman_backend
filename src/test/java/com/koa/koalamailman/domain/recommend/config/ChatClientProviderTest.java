package com.koa.koalamailman.domain.recommend.config;

import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ChatClientProviderTest {

    @Mock
    private ChatClient primaryChatClient;

    @Mock
    private ChatClient fallbackChatClient;

    private ChatClientProvider chatClientProvider;

    @BeforeEach
    void setUp() {
        chatClientProvider = new ChatClientProvider(primaryChatClient, fallbackChatClient);
    }

    @Nested
    @DisplayName("callWithFallback 동기 호출 테스트")
    class CallWithFallbackTest {

        @Test
        @DisplayName("Primary 성공 시 결과를 반환한다")
        void primarySuccess() {
            String result = chatClientProvider.callWithFallback(client -> {
                if (client == primaryChatClient) return "primary result";
                return "fallback result";
            });

            assertThat(result).isEqualTo("primary result");
        }

        @Test
        @DisplayName("Primary 실패 시 Fallback으로 결과를 반환한다")
        void fallbackOnPrimaryFailure() {
            String result = chatClientProvider.callWithFallback(client -> {
                if (client == primaryChatClient) throw new RuntimeException("Primary failed");
                return "fallback result";
            });

            assertThat(result).isEqualTo("fallback result");
        }

        @Test
        @DisplayName("모든 모델 실패 시 BusinessException을 던진다")
        void allModelsFailed() {
            assertThatThrownBy(() -> chatClientProvider.callWithFallback(client -> {
                throw new RuntimeException("Model failed");
            }))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(RecommendErrorCode.RECOMMEND_ALL_MODELS_FAILED);
        }
    }

    @Nested
    @DisplayName("streamWithFallback 스트리밍 호출 테스트")
    class StreamWithFallbackTest {

        @Test
        @DisplayName("Primary 성공 시 스트리밍 결과를 반환한다")
        void primarySuccess() {
            Flux<String> result = chatClientProvider.streamWithFallback(client -> {
                if (client == primaryChatClient) return Flux.just("goal1", "goal2");
                return Flux.just("fallback1");
            });

            StepVerifier.create(result)
                    .expectNext("goal1")
                    .expectNext("goal2")
                    .verifyComplete();
        }

        @Test
        @DisplayName("Primary 실패 시 Fallback 스트리밍으로 전환한다")
        void fallbackOnPrimaryFailure() {
            Flux<String> result = chatClientProvider.streamWithFallback(client -> {
                if (client == primaryChatClient) return Flux.error(new RuntimeException("Primary failed"));
                return Flux.just("fallback1", "fallback2");
            });

            StepVerifier.create(result)
                    .expectNext("fallback1")
                    .expectNext("fallback2")
                    .verifyComplete();
        }

        @Test
        @DisplayName("모든 모델 실패 시 에러를 반환한다")
        void allModelsFailed() {
            Flux<String> result = chatClientProvider.streamWithFallback(client ->
                    Flux.error(new RuntimeException("Model failed"))
            );

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getErrorCode() == RecommendErrorCode.RECOMMEND_ALL_MODELS_FAILED)
                    .verify();
        }
    }
}
