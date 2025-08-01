package com.koa.RingDong.domain.chatbot.controller;

import com.koa.RingDong.domain.chatbot.dto.ChatRequest;
import com.koa.RingDong.domain.chatbot.dto.ChatResponse;
import com.koa.RingDong.domain.chatbot.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatBotController {
    private final HuggingFaceService huggingFaceService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String response = huggingFaceService
                .chat(request.message())
                .block();

        return ChatResponse.from(response);
    }
}
