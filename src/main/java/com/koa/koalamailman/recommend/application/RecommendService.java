package com.koa.koalamailman.recommend.application;

import com.koa.koalamailman.recommend.infrastructure.PromptTemplates;
import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendService {

    private static final int MAX_GOAL_LENGTH = 40;

    private final ChatClient chatClient;

    public List<String> getChildGoalByParentGoal(String parentGoal, int recommendationCount, List<String> excludeGoals) {
        // age, gender, job 별 프롬프트 개인화 중지
        var response = buildChildGoalPrompt(parentGoal, recommendationCount, null, null, null, excludeGoals)
                .call()
                .content();

        if (response == null || response.isBlank()) {
            log.error("[Recommend] LLM 응답이 비어있습니다. parentGoal = {}, count = {}", parentGoal, recommendationCount);
            throw new BusinessException(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        List<String> goals = Arrays.stream(response.split(","))
                .map(String::trim)
                .filter(goal -> !goal.isEmpty())
                .map(this::truncateGoal)
                .toList();

        if (goals.isEmpty()) {
            log.error("[Recommend] LLM 응답 파싱 후 유효한 목표가 없습니다. parentGoal = {}, rawResponse = {}", parentGoal, response);
            throw new BusinessException(RecommendErrorCode.RECOMMEND_NOT_CONTENT);
        }

        return goals;
    }

    public Flux<String> streamingChildGoalByParentGoal(String parentGoal, int recommendationCount, List<String> excludeGoals) {
        AtomicReference<String> buffer = new AtomicReference<>("");

        // age, gender, job 별 프롬프트 개인화 중지
        return buildChildGoalPrompt(parentGoal, recommendationCount, null, null, null, excludeGoals)
                .stream()
                .content()
                .concatMap(chunk -> parseCompletedGoals(buffer, chunk))
                .concatWith(Flux.defer(() -> parseRemainingGoal(buffer)));
    }

    private Flux<String> parseCompletedGoals(AtomicReference<String> buffer, String chunk) {
        String current = buffer.get() + chunk;
        List<String> goals = new ArrayList<>();

        while (current.contains(",")) {
            int commaIndex = current.indexOf(',');
            String goal = current.substring(0, commaIndex).trim();
            if (!goal.isEmpty()) {
                goals.add(truncateGoal(goal));
            }
            current = current.substring(commaIndex + 1);
        }

        buffer.set(current);
        return Flux.fromIterable(goals);
    }

    private Flux<String> parseRemainingGoal(AtomicReference<String> buffer) {
        String remaining = buffer.get().trim();
        if (!remaining.isEmpty()) {
            return Flux.just(truncateGoal(remaining));
        }
        return Flux.empty();
    }

    private String truncateGoal(String goal) {
        return goal.length() > MAX_GOAL_LENGTH ? goal.substring(0, MAX_GOAL_LENGTH) : goal;
    }

    private ChatClient.ChatClientRequestSpec buildChildGoalPrompt(
            String parentGoal,
            int recommendationCount,
            AgeGroup ageGroup,
            Gender gender,
            String job,
            List<String> excludeGoals
    ) {
        String excludeGoalsStr = (excludeGoals == null || excludeGoals.isEmpty())
                ? "없음"
                : String.join(", ", excludeGoals);

        return chatClient.prompt()
                .user(u -> u
                        .text(PromptTemplates.Reference_Sub_By_Main)
                        .param("parentGoal", parentGoal)
                        .param("recommendationCount", recommendationCount)
                        .param("ageGroup", ageGroup != null ? ageGroup.toString() : "")
                        .param("gender", gender != null ? gender.toString() : "")
                        .param("job", job != null ? job : "")
                        .param("excludeGoals", excludeGoalsStr)
                );
    }
}
