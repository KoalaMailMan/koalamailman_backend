package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.SubGoal;
import com.koa.RingDong.domain.mandalart.repository.MainGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {
    List<SubGoal> findByMainGoal(MainGoal mainGoal);
}
