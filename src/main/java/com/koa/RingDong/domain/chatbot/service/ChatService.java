package com.koa.RingDong.domain.chatbot.service;

import com.koa.RingDong.domain.chatbot.template.PromptTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public Flux<String> referenceMainGoalByCategory(String category, String conversationId) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Main_By_Category)
                        .param("category", category))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    public  Flux<String> referenceSubGoalByMainGoal(String mainGoal, String conversationId) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("mainGoal", mainGoal))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }
}