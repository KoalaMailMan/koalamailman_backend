package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.CreateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateSubBlockRequest;
import com.koa.RingDong.dto.response.CellResponse;
import com.koa.RingDong.dto.response.MainBlockResponse;
import com.koa.RingDong.dto.response.SubBlockResponse;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.repository.CellRepository;
import com.koa.RingDong.repository.SubBlockRepository;
import com.koa.RingDong.repository.MainBlockRepository;
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
    public Long createMain(CreateMainBlockRequest request, Long userId) {
        MainBlock mainBlock = MainBlock.builder()
                .userId(userId)
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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getAllBlock(Long userId) {
        MainBlock mainBlock = mainBlockRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("메인 블럭이 존재하지 않습니다."));

        List<SubBlockResponse> subBlocks = subBlockRepository.findByMainBlock(mainBlock).stream()
                .map(sub -> {
                    List<CellResponse> cells = cellRepository.findBySubBlock(sub).stream()
                            .map(cell -> CellResponse.builder()
                                    .position(cell.getPosition())
                                    .content(cell.getContent())
                                    .status(cell.getStatus())
                                    .build())
                            .toList();

                    return SubBlockResponse.builder()
                            .subId(sub.getSubId())
                            .position(sub.getPosition())
                            .content(sub.getContent())
                            .status(sub.getStatus())
                            .cellResponses(cells)
                            .build();
                })
                .toList();

        return MainBlockResponse.builder()
                .mainId(mainBlock.getMainId())
                .userId(mainBlock.getUserId())
                .content(mainBlock.getContent())
                .status(mainBlock.getStatus())
                .subBlockResponses(subBlocks)
                .build();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getMainBlock(Long userId) {
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
                .subBlockResponses(subBlocks)
                .build();
    }

    @Transactional
    public void updateMain(Long mainId, UpdateMainBlockRequest request) {
        MainBlock mainBlock = mainBlockRepository.findById(mainId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메인 블럭이 존재하지 않습니다."));

        // 메인 블럭 필드 업데이트
        mainBlock.setContent(request.getContent());

        // 각 서브 블럭 업데이트
        for (UpdateSubBlockRequest subReq : request.getSubBlockRequests()) {
            SubBlock subBlock = subBlockRepository.findByMainBlockAndPosition(mainBlock, subReq.getPosition())
                    .orElseThrow(() -> new IllegalArgumentException("position " + subReq.getPosition() + "에 해당하는 서브 블럭이 없습니다."));

            subBlock.setContent(subReq.getContent());
            subBlock.setStatus(subReq.getStatus());
        }
    }
}
