package com.koa.koalamailman.recommend.application;

import com.koa.koalamailman.recommend.infrastructure.ParentGoalCacheRepository.CacheResult;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendService {

    private static final int MAX_GOAL_LENGTH = 40;

    private final ChatClient chatClient;
    private final GoalEmbeddingCacheService embeddingCacheService;

    public List<String> getChildGoalByParentGoal(String parentGoal, int recommendationCount, AgeGroup ageGroup, Gender gender, String job, List<String> excludeGoals) {
        Optional<CacheResult> cacheResult = embeddingCacheService.findCachedResult(parentGoal);

        if (cacheResult.isPresent()) {
            List<String> cachedGoals = filterExcluded(cacheResult.get().childGoals(), excludeGoals);

            if (cachedGoals.size() >= recommendationCount) {
                return cachedGoals;
            }

            // 캐시 히트지만 개수 부족 → LLM으로 부족한 만큼 추가 요청
            int remaining = recommendationCount - cachedGoals.size();
            List<String> combinedExcludes = buildCombinedExcludes(excludeGoals, cachedGoals);
            List<String> additionalGoals = callLLM(parentGoal, remaining, ageGroup, gender, job, combinedExcludes);

            embeddingCacheService.appendGoals(cacheResult.get().id(), additionalGoals);

            List<String> merged = new ArrayList<>(cachedGoals);
            merged.addAll(additionalGoals);
            return merged;
        }

        List<String> goals = callLLM(parentGoal, recommendationCount, ageGroup, gender, job, excludeGoals);
        embeddingCacheService.cacheGoals(parentGoal, goals);
        return goals;
    }

    public Flux<String> streamingChildGoalByParentGoal(String parentGoal, int recommendationCount, AgeGroup ageGroup, Gender gender, String job, List<String> excludeGoals) {
        Optional<CacheResult> cacheResult = embeddingCacheService.findCachedResult(parentGoal);

        if (cacheResult.isPresent()) {
            List<String> cachedGoals = filterExcluded(cacheResult.get().childGoals(), excludeGoals);

            if (cachedGoals.size() >= recommendationCount) {
                return Flux.fromIterable(cachedGoals);
            }

            // 캐시 히트지만 개수 부족 → LLM으로 나머지 스트리밍
            int remaining = recommendationCount - cachedGoals.size();
            List<String> combinedExcludes = buildCombinedExcludes(excludeGoals, cachedGoals);
            AtomicReference<String> buffer = new AtomicReference<>("");
            List<String> additionalGoals = new ArrayList<>();

            return Flux.fromIterable(cachedGoals)
                    .concatWith(
                            buildChildGoalPrompt(parentGoal, remaining, ageGroup, gender, job, combinedExcludes)
                                    .stream()
                                    .content()
                                    .concatMap(chunk -> parseCompletedGoals(buffer, chunk))
                                    .concatWith(Flux.defer(() -> parseRemainingGoal(buffer)))
                                    .doOnNext(additionalGoals::add)
                                    .doOnComplete(() -> embeddingCacheService.appendGoals(cacheResult.get().id(), additionalGoals))
                    );
        }

        AtomicReference<String> buffer = new AtomicReference<>("");
        List<String> collectedGoals = new ArrayList<>();

        return buildChildGoalPrompt(parentGoal, recommendationCount, ageGroup, gender, job, excludeGoals)
                .stream()
                .content()
                .concatMap(chunk -> parseCompletedGoals(buffer, chunk))
                .concatWith(Flux.defer(() -> parseRemainingGoal(buffer)))
                .doOnNext(collectedGoals::add)
                .doOnComplete(() -> embeddingCacheService.cacheGoals(parentGoal, collectedGoals));
    }

    private List<String> callLLM(String parentGoal, int recommendationCount, AgeGroup ageGroup, Gender gender, String job, List<String> excludeGoals) {
        var response = buildChildGoalPrompt(parentGoal, recommendationCount, ageGroup, gender, job, excludeGoals)
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

    private List<String> buildCombinedExcludes(List<String> excludeGoals, List<String> cachedGoals) {
        List<String> combined = new ArrayList<>(cachedGoals);
        if (excludeGoals != null) {
            combined.addAll(excludeGoals);
        }
        return combined;
    }

    private List<String> filterExcluded(List<String> goals, List<String> excludeGoals) {
        if (excludeGoals == null || excludeGoals.isEmpty()) {
            return goals;
        }
        return goals.stream()
                .filter(goal -> !excludeGoals.contains(goal))
                .toList();
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