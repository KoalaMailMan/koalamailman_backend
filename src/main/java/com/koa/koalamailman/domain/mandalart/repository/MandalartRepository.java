package com.koa.koalamailman.domain.mandalart.repository;

import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MandalartRepository extends JpaRepository<MandalartEntity, Long> {
    Optional<MandalartEntity> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
}
