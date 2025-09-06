package com.koa.koalamailman.domain.mandalart.repository;

import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MandalartRepository extends JpaRepository<MandalartEntity, Long> {
    Optional<MandalartEntity> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
    @Query("select m from MandalartEntity m " +
            "where m.reminderOption.reminderEnabled = true " +
            "and m.reminderOption.remindScheduledAt <= :endOfDay")
    List<MandalartEntity> findDueReminders(@Param("endOfDay") LocalDateTime endOfDay);}
