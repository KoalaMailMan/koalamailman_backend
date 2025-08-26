package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.entity.CoreGoal;
import com.koa.RingDong.domain.mandalart.repository.entity.MainGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainGoalRepository extends JpaRepository<MainGoal, Long> {
    List<MainGoal> findByCoreGoal(CoreGoal coreGoal);
    Optional<MainGoal> findByMainGoalId(Long mainGoalId);
    Optional<MainGoal> findByCoreGoalAndPosition(CoreGoal coreGoal, int position);

}