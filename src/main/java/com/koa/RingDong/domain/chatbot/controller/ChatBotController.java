package com.koa.RingDong.domain.chatbot.controller;

import com.koa.RingDong.domain.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatBotController {
    private final ChatService chatService;


    @GetMapping(value = "/reference/main", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> generationStreamingMainGoal(
            @RequestParam("category") String category,
            @RequestParam("conversationId") String conversationId
    ) {
        return chatService.referenceMainGoalByCategory(category, conversationId);
    }

    @GetMapping(value = "/reference/sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> generationStreamingSubGoal(
            @RequestParam("goal") String mainGoal,
            @RequestParam("conversationId") String conversationId
    ) {
        return chatService.referenceSubGoalByMainGoal(mainGoal, conversationId);
    }
}
