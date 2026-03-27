package com.koa.koalamailman.recommend.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParentGoalCacheRepository {

    private static final int TOP_K = 5;

    private final JdbcClient jdbcClient;

    /**
     * 코사인 유사도 기준 상위 K개를 조회한 뒤 threshold 이상인 가장 유사한 항목을 반환합니다.
     */
    public Optional<CacheResult> findMostSimilar(float[] embedding, double threshold) {
        String vectorLiteral = toPgVectorLiteral(embedding);

        return jdbcClient.sql("""
                WITH sims AS (
                    SELECT id,
                           child_goals,
                           1 - (embedding <=> CAST(:vec AS vector)) AS similarity
                    FROM parent_goal_cache
                )
                SELECT *
                FROM sims
                WHERE similarity >= :threshold
                ORDER BY similarity DESC
                LIMIT :topK
                """)
                .param("vec", vectorLiteral)
                .param("threshold", threshold)
                .param("topK", TOP_K)
                .query((rs, rowNum) -> new CacheResult(
                        rs.getLong("id"),
                        toStringList(rs.getArray("child_goals")),
                        rs.getDouble("similarity")
                ))
                .list()
                .stream()
                .findFirst();
    }

    public void save(String parentGoal, float[] embedding, List<String> childGoals) {
        jdbcClient.sql("""
                INSERT INTO parent_goal_cache (parent_goal, embedding, child_goals)
                VALUES (:parentGoal, CAST(:vec AS vector), :goals)
                ON CONFLICT DO NOTHING
                """)
                .param("parentGoal", parentGoal)
                .param("vec", toPgVectorLiteral(embedding))
                .param("goals", childGoals.toArray(new String[0]))
                .update();
    }

    /**
     * 기존 캐시 항목에 새 child goals를 추가합니다.
     */
    public void appendGoals(Long id, List<String> additionalGoals) {
        jdbcClient.sql("""
                UPDATE parent_goal_cache
                SET child_goals = child_goals || CAST(:additionalGoals AS text[])
                WHERE id = :id
                """)
                .param("additionalGoals", additionalGoals.toArray(new String[0]))
                .param("id", id)
                .update();
    }

    private List<String> toStringList(java.sql.Array sqlArray) throws java.sql.SQLException {
        Object[] arr = (Object[]) sqlArray.getArray();
        return Arrays.stream(arr)
                .map(String.class::cast)
                .toList();
    }

    private String toPgVectorLiteral(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public record CacheResult(Long id, List<String> childGoals, double similarity) {}
}