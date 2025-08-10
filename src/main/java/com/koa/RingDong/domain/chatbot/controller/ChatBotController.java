package com.koa.RingDong.domain.chatbot.controller;

import com.koa.RingDong.domain.chatbot.dto.ChatRequest;
import com.koa.RingDong.domain.chatbot.service.OpenRouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatBotController {
    private final OpenRouterService openRouterService;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return openRouterService.callLLM(request.message());
    }
}
