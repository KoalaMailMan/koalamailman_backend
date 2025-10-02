package com.koa.koalamailman.domain.recommend.service;

import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.domain.recommend.session.ProfileSession;
import com.koa.koalamailman.domain.recommend.template.PromptTemplates;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendService {

    private final ChatClient chatClient;

    public ChildGoalsResponse getChildGoalByParentGoal(String parentGoal, int recommendationCount) {
        var response = chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("parentGoal", parentGoal)
                        .param("recommendationCount", recommendationCount)
                )
                .call();

        if (response.content() == null || response.content().isBlank()) {
            log.error("[Recommend] LLM 응답이 비어있습니다. parentGoal = {}, count = {}", parentGoal, recommendationCount);
            throw new BusinessException(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        return response.entity(ChildGoalsResponse.class);
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
