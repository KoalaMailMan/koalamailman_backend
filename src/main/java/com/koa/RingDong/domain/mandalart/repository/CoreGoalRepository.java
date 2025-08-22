package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.CoreGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoreGoalRepository extends JpaRepository<CoreGoal, Long> {
    Optional<CoreGoal> findByUserId(Long userId);

    @Query("""
        SELECT c FROM CoreGoal c
        LEFT JOIN FETCH c.mainGoals mg
        LEFT JOIN FETCH mg.subGoals
        WHERE c.userId = :userId
    """)
    Optional<CoreGoal> findCoreGoalWithMainGoalsByUserId(@Param("userId") Long userId);

    List<CoreGoal> findByNextScheduledTimeBetween(LocalDateTime start, LocalDateTime end);
    List<CoreGoal> findByNextScheduledTimeBefore(LocalDateTime time);


}
