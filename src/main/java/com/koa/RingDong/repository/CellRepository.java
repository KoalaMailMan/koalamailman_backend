package com.koa.RingDong.repository;

import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.SubBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    List<Cell> findBySubBlock(SubBlock subBlock);
}
