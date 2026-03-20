package com.koa.koalamailman.domain.recommend.config;

import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class ChatClientProvider {

    private final List<ChatClient> chatClients;

    public ChatClientProvider(
            @Qualifier("primaryChatClient") ChatClient primaryChatClient,
            @Qualifier("fallbackChatClient") ChatClient fallbackChatClient
    ) {
        this.chatClients = List.of(primaryChatClient, fallbackChatClient);
    }

    public String callWithFallback(Function<ChatClient, String> action) {
        Exception lastException = null;

        for (int i = 0; i < chatClients.size(); i++) {
            try {
                String result = action.apply(chatClients.get(i));
                if (i > 0) {
                    log.info("[ChatClientProvider] Fallback 모델(index={})로 호출 성공", i);
                }
                return result;
            } catch (Exception e) {
                lastException = e;
                log.warn("[ChatClientProvider] 모델(index={}) 호출 실패: {}", i, e.getMessage());
            }
        }

        log.error("[ChatClientProvider] 모든 모델 호출 실패", lastException);
        throw new BusinessException(RecommendErrorCode.RECOMMEND_ALL_MODELS_FAILED);
    }

    public Flux<String> streamWithFallback(Function<ChatClient, Flux<String>> action) {
        return streamWithFallbackRecursive(action, 0);
    }

    private Flux<String> streamWithFallbackRecursive(Function<ChatClient, Flux<String>> action, int index) {
        if (index >= chatClients.size()) {
            return Flux.error(new BusinessException(RecommendErrorCode.RECOMMEND_ALL_MODELS_FAILED));
        }

        return action.apply(chatClients.get(index))
                .doOnSubscribe(s -> {
                    if (index > 0) {
                        log.info("[ChatClientProvider] Fallback 모델(index={})로 스트리밍 시도", index);
                    }
                })
                .onErrorResume(e -> {
                    log.warn("[ChatClientProvider] 스트리밍 모델(index={}) 호출 실패: {}", index, e.getMessage());
                    return streamWithFallbackRecursive(action, index + 1);
                });
    }
}