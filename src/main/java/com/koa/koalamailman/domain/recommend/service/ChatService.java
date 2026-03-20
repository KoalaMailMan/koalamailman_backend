package com.koa.koalamailman.domain.recommend.service;

import com.koa.koalamailman.domain.recommend.session.ProfileSession;
import com.koa.koalamailman.domain.recommend.template.PromptTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(@Qualifier("primaryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

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