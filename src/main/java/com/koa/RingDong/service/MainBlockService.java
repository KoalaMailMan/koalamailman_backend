package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.CreateMainBlockRequest;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.repository.SubBlockRepository;
import com.koa.RingDong.repository.MainBlockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MainBlockService {

    private final MainBlockRepository mainBlockRepository;
    private final SubBlockRepository subBlockRepository;

    @Transactional
    public Long createMainBlock(CreateMainBlockRequest request) {
        MainBlock mainBlock = MainBlock.builder()
                .userId(request.getUserId())
                .content(request.getContent())
                .build();
        mainBlockRepository.save(mainBlock);

        List<SubBlock> subBlocks = IntStream.range(0, 8)
                .mapToObj(pos -> SubBlock.builder()
                        .mainBlock(mainBlock)
                        .position(pos)
                        .build())
                .toList();
        subBlockRepository.saveAll(subBlocks);

        return mainBlock.getMainId();
    }
}
