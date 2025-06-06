package com.koa.RingDong.repository;

import com.koa.RingDong.entity.MainBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainBlockRepository extends JpaRepository<MainBlock, Long> {
    Optional<MainBlock> findByUserId(Long userId);
}
