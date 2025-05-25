package com.koa.RingDong.repository;

import com.koa.RingDong.entity.SubBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubBlockRepository extends JpaRepository<SubBlock, Long> {

    // 특정 만다라트에 속한 블럭 전체 조회
    List<SubBlock> findByMandalart_MandalartId(Long mandalartId);

    // 특정 만다라트 + 포지션으로 블럭 단건 조회
    Optional<SubBlock> findByMandalart_MandalartIdAndPosition(Long mandalartId, Integer position);
}