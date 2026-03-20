package com.koa.koalamailman.domain.recommend.service;

import com.koa.koalamailman.domain.recommend.config.ChatClientProvider;
import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
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

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {

    @Mock
    private ChatClientProvider chatClientProvider;

    private RecommendService recommendService;

    @BeforeEach
    void setUp() {
        recommendService = new RecommendService(chatClientProvider);
    }

    @Nested
    @DisplayName("getChildGoalByParentGoal 동기 호출 테스트")
    class GetChildGoalByParentGoalTest {

        @Test
        @DisplayName("정상 응답 시 목표 리스트를 반환한다")
        void success() {
            // given
            mockCallWithFallback("목표1, 목표2, 목표3");

            // when
            ChildGoalsResponse result = recommendService.getChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null);

            // then
            assertThat(result.childGoals()).hasSize(3);
            assertThat(result.childGoals()).containsExactly("목표1", "목표2", "목표3");
        }

        @Test
        @DisplayName("응답에 공백이 포함되어도 trim 처리된다")
        void trimWhitespace() {
            // given
            mockCallWithFallback("  목표1  ,  목표2  ,  목표3  ");

            // when
            ChildGoalsResponse result = recommendService.getChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null);

            // then
            assertThat(result.childGoals()).containsExactly("목표1", "목표2", "목표3");
        }

        @Test
        @DisplayName("빈 문자열은 필터링된다")
        void filterEmptyStrings() {
            // given
            mockCallWithFallback("목표1,,  ,목표2");

            // when
            ChildGoalsResponse result = recommendService.getChildGoalByParentGoal(
                    "상위목표", 2, null, null, null, null);

            // then
            assertThat(result.childGoals()).hasSize(2);
            assertThat(result.childGoals()).containsExactly("목표1", "목표2");
        }

        @Test
        @DisplayName("40자를 초과하는 목표는 40자로 잘린다")
        void truncateLongGoals() {
            // given
            String longGoal = "이것은매우긴목표입니다".repeat(5); // 55자
            mockCallWithFallback(longGoal + ", 짧은목표");

            // when
            ChildGoalsResponse result = recommendService.getChildGoalByParentGoal(
                    "상위목표", 2, null, null, null, null);

            // then
            assertThat(result.childGoals().get(0)).hasSize(40);
            assertThat(result.childGoals().get(1)).isEqualTo("짧은목표");
        }

        @Test
        @DisplayName("응답이 null이면 예외를 던진다")
        void throwsWhenResponseIsNull() {
            // given
            mockCallWithFallback(null);

            // when & then
            assertThatThrownBy(() -> recommendService.getChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        @Test
        @DisplayName("응답이 비어있으면 예외를 던진다")
        void throwsWhenResponseIsBlank() {
            // given
            mockCallWithFallback("   ");

            // when & then
            assertThatThrownBy(() -> recommendService.getChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        @Test
        @DisplayName("파싱 후 유효한 목표가 없으면 예외를 던진다")
        void throwsWhenNoValidGoalsAfterParsing() {
            // given
            mockCallWithFallback("  ,  ,  ");

            // when & then
            assertThatThrownBy(() -> recommendService.getChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        @SuppressWarnings("unchecked")
        private void mockCallWithFallback(String response) {
            when(chatClientProvider.callWithFallback(any(Function.class)))
                    .thenAnswer(invocation -> {
                        Function<ChatClient, String> action = invocation.getArgument(0);
                        ChatClient mockClient = mockChatClientForCall(response);
                        return action.apply(mockClient);
                    });
        }

        private ChatClient mockChatClientForCall(String response) {
            ChatClient chatClient = mock(ChatClient.class);
            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

            when(chatClient.prompt()).thenReturn(requestSpec);
            when(requestSpec.user(any(java.util.function.Consumer.class))).thenReturn(requestSpec);
            when(requestSpec.call()).thenReturn(callResponseSpec);
            when(callResponseSpec.content()).thenReturn(response);

            return chatClient;
        }
    }

    @Nested
    @DisplayName("streamingChildGoalByParentGoal 스트리밍 호출 테스트")
    class StreamingChildGoalByParentGoalTest {

        @Test
        @DisplayName("스트리밍 응답을 쉼표 단위로 파싱한다")
        void parseByComma() {
            // given
            mockStreamWithFallback(Flux.just("목표1,", "목표2,", "목표3"));

            // when
            Flux<String> result = recommendService.streamingChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null);

            // then
            StepVerifier.create(result)
                    .expectNext("목표1")
                    .expectNext("목표2")
                    .expectNext("목표3")
                    .verifyComplete();
        }

        @Test
        @DisplayName("청크가 쪼개져 와도 올바르게 파싱한다")
        void handleSplitChunks() {
            // given
            mockStreamWithFallback(Flux.just("목", "표1,목표", "2,목표3"));

            // when
            Flux<String> result = recommendService.streamingChildGoalByParentGoal(
                    "상위목표", 3, null, null, null, null);

            // then
            StepVerifier.create(result)
                    .expectNext("목표1")
                    .expectNext("목표2")
                    .expectNext("목표3")
                    .verifyComplete();
        }

        @Test
        @DisplayName("40자를 초과하는 목표는 잘린다")
        void truncateLongGoals() {
            // given
            String longGoal = "가나다라마바사아자차".repeat(5); // 50자
            mockStreamWithFallback(Flux.just(longGoal + ",짧은목표"));

            // when
            Flux<String> result = recommendService.streamingChildGoalByParentGoal(
                    "상위목표", 2, null, null, null, null);

            // then
            StepVerifier.create(result)
                    .assertNext(goal -> assertThat(goal).hasSize(40))
                    .expectNext("짧은목표")
                    .verifyComplete();
        }

        @Test
        @DisplayName("빈 문자열은 필터링된다")
        void filterEmptyStrings() {
            // given
            mockStreamWithFallback(Flux.just("목표1,  ,목표2,  "));

            // when
            Flux<String> result = recommendService.streamingChildGoalByParentGoal(
                    "상위목표", 2, null, null, null, null);

            // then
            StepVerifier.create(result)
                    .expectNext("목표1")
                    .expectNext("목표2")
                    .verifyComplete();
        }

        @SuppressWarnings("unchecked")
        private void mockStreamWithFallback(Flux<String> streamContent) {
            when(chatClientProvider.streamWithFallback(any(Function.class)))
                    .thenAnswer(invocation -> {
                        Function<ChatClient, Flux<String>> action = invocation.getArgument(0);
                        ChatClient mockClient = mockChatClientForStream(streamContent);
                        return action.apply(mockClient);
                    });
        }

        private ChatClient mockChatClientForStream(Flux<String> streamContent) {
            ChatClient chatClient = mock(ChatClient.class);
            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.StreamResponseSpec streamResponseSpec = mock(ChatClient.StreamResponseSpec.class);

            when(chatClient.prompt()).thenReturn(requestSpec);
            when(requestSpec.user(any(java.util.function.Consumer.class))).thenReturn(requestSpec);
            when(requestSpec.stream()).thenReturn(streamResponseSpec);
            when(streamResponseSpec.content()).thenReturn(streamContent);

            return chatClient;
        }
    }
}
