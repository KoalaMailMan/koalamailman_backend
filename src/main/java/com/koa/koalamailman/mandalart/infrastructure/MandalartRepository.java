package com.koa.koalamailman.mandalart.infrastructure;

import com.koa.koalamailman.mandalart.domain.Mandalart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MandalartRepository extends JpaRepository<Mandalart, Long> {
    Optional<Mandalart> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
    @Query("select m from Mandalart m " +
            "where m.reminderOption.reminderEnabled = true " +
            "and m.reminderOption.remindScheduledAt <= :endOfDay")
    List<Mandalart> findDueReminders(@Param("endOfDay") LocalDateTime endOfDay);}
