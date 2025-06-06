package com.koa.RingDong.repository;

import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.SubBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubBlockRepository extends JpaRepository<SubBlock, Long> {
    List<SubBlock> findByMainBlock(MainBlock mainBlock);
    Optional<SubBlock> findByMainBlockAndPosition(MainBlock mainBlock, int position);

}