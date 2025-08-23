package com.koa.RingDong.domain.chatbot.service;

import com.koa.RingDong.domain.chatbot.session.ProfileSession;
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

    public Flux<String> referenceMainGoalByCategory(String category, ProfileSession profileSession) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Main_By_Category)
                        .param("category", category)
                        .param("ageGroup", profileSession.getAgeGroup())
                        .param("gender", profileSession.getGender())
                        .param("job", profileSession.getJob())
                )
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, profileSession.getConversationId()))
                .stream()
                .content();
    }

    public  Flux<String> referenceSubGoalByMainGoal(String mainGoal, ProfileSession profileSession) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("mainGoal", mainGoal)
                        .param("ageGroup", profileSession.getAgeGroup())
                        .param("gender", profileSession.getGender())
                        .param("job", profileSession.getJob())
                )
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, profileSession.getConversationId()))
                .stream()
                .content();
    }
}