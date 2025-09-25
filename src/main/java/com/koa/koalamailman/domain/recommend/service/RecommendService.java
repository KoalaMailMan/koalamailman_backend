package com.koa.koalamailman.domain.recommend.service;

import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.domain.recommend.session.ProfileSession;
import com.koa.koalamailman.domain.recommend.template.PromptTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final ChatClient chatClient;

    public ChildGoalsResponse getChildGoalByParentGoal(String parentGoal, int recommendationCount) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("parentGoal", parentGoal)
                        .param("recommendationCount", recommendationCount)
                )
                .call()
                .entity(ChildGoalsResponse.class);
    }

    public Flux<String> streamingChildGoalByParentGoal(String parentGoal, int recommendationCount) {
        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("parentGoal", parentGoal)
                        .param("recommendationCount", recommendationCount)
                )
                .stream()
                .content();
    }

}
