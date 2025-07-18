package com.koa.RingDong.domain.mandalart.service;

import com.koa.RingDong.domain.mandalart.dto.MainBlockResponse;
import com.koa.RingDong.domain.mandalart.dto.SubBlockResponse;
import com.koa.RingDong.domain.mandalart.repository.SubBlock;
import com.koa.RingDong.domain.mandalart.repository.MainBlock;
import com.koa.RingDong.domain.mandalart.repository.CellRepository;
import com.koa.RingDong.domain.mandalart.repository.SubBlockRepository;
import com.koa.RingDong.domain.mandalart.repository.MainBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MainBlockService {

    private final MainBlockRepository mainBlockRepository;
    private final SubBlockRepository subBlockRepository;
    private final CellRepository cellRepository;

    @Transactional
    public MainBlock createMainArea(Long userId) {
        MainBlock mainBlock = MainBlock.builder()
                .userId(userId)
                .build();
        mainBlockRepository.save(mainBlock);

        List<SubBlock> subBlocks = IntStream.range(0, 8)
                .mapToObj(pos -> SubBlock.builder()
                        .mainBlock(mainBlock)
                        .position(pos)
                        .build())
                .toList();
        subBlockRepository.saveAll(subBlocks);

        return mainBlock;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getMainArea(Long userId) {
        MainBlock mainBlock = mainBlockRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("메인 블럭이 존재하지 않습니다."));

        List<SubBlockResponse> subBlocks = subBlockRepository.findByMainBlock(mainBlock).stream()
                .map(sub -> SubBlockResponse.builder()
                        .position(sub.getPosition())
                        .content(sub.getContent())
                        .build())
                .toList();

        return MainBlockResponse.builder()
                .mainId(mainBlock.getMainId())
                .userId(mainBlock.getUserId())
                .content(mainBlock.getContent())
                .status(mainBlock.getStatus())
                .subBlocks(subBlocks)
                .build();
    }
}
