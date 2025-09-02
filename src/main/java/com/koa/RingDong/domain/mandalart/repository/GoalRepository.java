package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.entity.GoalEntity;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    List<GoalEntity> findAllByMandalartId(@Param("mandalartId") Long mandalartId);

    Optional<GoalEntity> findByMandalartIdAndLevelAndPosition(Long mandalartId, GoalLevel level, Integer position);
}
