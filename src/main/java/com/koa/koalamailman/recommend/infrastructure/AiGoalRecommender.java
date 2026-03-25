package com.koa.koalamailman.recommend.infrastructure;

import com.koa.koalamailman.recommend.application.port.GoalRecommender;
import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AiGoalRecommender implements GoalRecommender {

    private final ChatClient chatClient;

    @Override
    public String call(String parentGoal, int count, List<String> excludeGoals) {
        return buildPrompt(parentGoal, count, null, null, null, excludeGoals)
                .call()
                .content();
    }

    @Override
    public Flux<String> stream(String parentGoal, int count, List<String> excludeGoals) {
        return buildPrompt(parentGoal, count, null, null, null, excludeGoals)
                .stream()
                .content();
    }

    private ChatClient.ChatClientRequestSpec buildPrompt(
            String parentGoal, int count, AgeGroup ageGroup, Gender gender, String job,
            List<String> excludeGoals) {
        String excludeGoalsStr = (excludeGoals == null || excludeGoals.isEmpty())
                ? "없음"
                : String.join(", ", excludeGoals);

        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("parentGoal", parentGoal)
                        .param("recommendationCount", count)
                        .param("ageGroup", ageGroup != null ? ageGroup.toString() : "")
                        .param("gender", gender != null ? gender.toString() : "")
                        .param("job", job != null ? job : "")
                        .param("excludeGoals", excludeGoalsStr)
                );
    }
}
