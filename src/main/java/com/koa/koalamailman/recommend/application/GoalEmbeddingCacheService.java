package com.koa.koalamailman.recommend.application;

import com.koa.koalamailman.recommend.infrastructure.ParentGoalCacheRepository;
import com.koa.koalamailman.recommend.infrastructure.ParentGoalCacheRepository.CacheResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalEmbeddingCacheService {

    private static final double SIMILARITY_THRESHOLD = 0.80;

    private final EmbeddingModel embeddingModel;
    private final ParentGoalCacheRepository cacheRepository;

    /**
     * parentGoal의 embedding을 생성하고 유사한 캐시가 존재하면 반환합니다.
     */
    public Optional<CacheResult> findCachedResult(String parentGoal) {
        float[] embedding = embed(parentGoal);
        return cacheRepository.findMostSimilar(embedding, SIMILARITY_THRESHOLD)
                .map(result -> {
                    log.info("[EmbeddingCache] 캐시 히트: parentGoal='{}', similarity={}", parentGoal, result.similarity());
                    return result;
                });
    }

    /**
     * 새 추천 결과를 embedding과 함께 저장합니다.
     */
    public void cacheGoals(String parentGoal, List<String> childGoals) {
        float[] embedding = embed(parentGoal);
        cacheRepository.save(parentGoal, embedding, childGoals);
        log.debug("[EmbeddingCache] 저장 완료: parentGoal='{}'", parentGoal);
    }

    /**
     * 기존 캐시 항목에 새 child goals를 추가합니다.
     */
    public void appendGoals(Long cacheId, List<String> additionalGoals) {
        cacheRepository.appendGoals(cacheId, additionalGoals);
        log.debug("[EmbeddingCache] 캐시 추가: id={}, 추가 목표 수={}", cacheId, additionalGoals.size());
    }

    private float[] embed(String text) {
        return embeddingModel.embed(text);
    }
}