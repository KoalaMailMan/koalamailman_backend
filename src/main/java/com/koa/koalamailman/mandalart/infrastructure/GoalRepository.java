package com.koa.koalamailman.mandalart.infrastructure;

import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.GoalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findGoalsByMandalartId(@Param("mandalartId") Long mandalartId);
    List<Goal> findByMandalartIdAndLevelIn(Long mandalartId, List<GoalLevel> levels);

    /**
     select g.*
     from goal g
     join mandalart m on g.mandalart_id = m.mandalart_id
     where m.user_id = ?
    **/
    @Query("""
        select g
        from Goal g
        join fetch g.mandalart m
        where m.userId = :userId
    """)
    List<Goal> findGoalsByUserId(@Param("userId") Long userId);
}
