package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.MainBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MainBlockRepository extends JpaRepository<MainBlock, Long> {
    Optional<MainBlock> findByUserId(Long userId);

    @Query("""
        SELECT m FROM MainBlock m
        LEFT JOIN FETCH m.subBlocks sb
        LEFT JOIN FETCH sb.cells
        WHERE m.userId = :userId
    """)
    Optional<MainBlock> findFullMandalartByUserId(@Param("userId") Long userId);

    List<MainBlock> findByNextScheduledTimeBetween(LocalDateTime start, LocalDateTime end);
    List<MainBlock> findByNextScheduledTimeBefore(LocalDateTime time);


}
