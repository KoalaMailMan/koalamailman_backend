package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.*;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.repository.SubBlockRepository;
import com.koa.RingDong.repository.CellRepository;
import com.koa.RingDong.repository.MainBlockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubBlockService {

    private final MainBlockRepository mainBlockRepository;
    private final SubBlockRepository subBlockRepository;
    private final CellRepository cellRepository;

    @Transactional
    public Long createSubBlock(Long mainId, CreateSubBlockRequest request) {
        MainBlock mainBlock = mainBlockRepository.findById(mainId)
                .orElseThrow(() -> new IllegalArgumentException("MainBlock not found"));

        SubBlock subBlock = SubBlock.builder()
                .mainBlock(mainBlock)
                .position(request.getPosition())
                .content(request.getContent())
                .build();

        subBlockRepository.save(subBlock);

        List<Cell> cells = request.getCells().stream()
                .map(c -> Cell.builder()
                        .subBlock(subBlock)
                        .position(c.getPosition())
                        .content(c.getContent())
                        .build())
                .toList();

        cellRepository.saveAll(cells);
        return subBlock.getSubId();
    }
}