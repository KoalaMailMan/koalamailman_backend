package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.entity.MainGoal;
import com.koa.RingDong.domain.mandalart.repository.entity.SubGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {
    List<SubGoal> findByMainGoal(MainGoal mainGoal);
}
