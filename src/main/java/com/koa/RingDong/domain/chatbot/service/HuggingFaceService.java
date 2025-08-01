package com.koa.RingDong.domain.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HuggingFaceService {
    private final WebClient huggingFaceWebClient;

    public Mono<String> chat(String userInput) {

        Map<String, Object> body = Map.of(
                "model", "HuggingFaceH4/zephyr-7b-alpha:featherless-ai",
                "messages", List.of(
                        Map.of("role", "user", "content", userInput)
                )
        );

        return huggingFaceWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }
}