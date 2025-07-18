package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.MainBlock;
import com.koa.RingDong.domain.mandalart.repository.SubBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubBlockRepository extends JpaRepository<SubBlock, Long> {
    List<SubBlock> findByMainBlock(MainBlock mainBlock);
    Optional<SubBlock> findBySubId(Long subId);
    Optional<SubBlock> findByMainBlockAndPosition(MainBlock mainBlock, int position);

}