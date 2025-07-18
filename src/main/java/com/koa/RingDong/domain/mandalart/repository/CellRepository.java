package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.repository.Cell;
import com.koa.RingDong.domain.mandalart.repository.SubBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    List<Cell> findBySubBlock(SubBlock subBlock);
}
