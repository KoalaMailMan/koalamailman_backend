package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.UpdateCellRequest;
import com.koa.RingDong.dto.request.UpdateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateSubBlockRequest;
import com.koa.RingDong.dto.response.MainBlockResponse;
import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.repository.MainBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MandalartService {

    private final MainBlockRepository mainBlockRepository;

    @Transactional()
    public void createMandalart(Long userId) {

        MainBlock mainBlock = MainBlock.builder()
                .userId(userId)
                .build();

        Set<SubBlock> subBlocks = new HashSet<>();

        IntStream.range(0, 8).forEach(pos -> {
            SubBlock subBlock = SubBlock.builder()
                    .mainBlock(mainBlock)
                    .position(pos)
                    .build();

            subBlocks.add(subBlock);
        });

        mainBlock.getSubBlocks().addAll(subBlocks);

        subBlocks.forEach(subBlock -> {
            Set<Cell> cells = new HashSet<>();
            IntStream.range(0, 8).forEach(cellPos -> {
                Cell cell = Cell.builder()
                        .subBlock(subBlock)
                        .position(cellPos)
                        .build();
                cells.add(cell);
            });
            subBlock.getCells().addAll(cells);
        });

        mainBlockRepository.save(mainBlock);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getMandalart(Long userId) {
        MainBlock mainBlock = mainBlockRepository.findFullMandalartByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Main Block not found"));

        return MainBlockResponse.from(mainBlock);
    }

    @Transactional
    public void updateMandalart(Long userId, UpdateMainBlockRequest request) {
        MainBlock mainBlock = mainBlockRepository.findFullMandalartByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Main Block not found"));

        // MainBlock 업데이트
        if (request.getContent() != null) {
            mainBlock.setContent(request.getContent());
        }
        if (request.getStatus() != null) {
            mainBlock.setStatus(request.getStatus());
        }
        if (request.getReminderInterval() != null) {
            mainBlock.setReminderInterval(request.getReminderInterval());
        }

        // SubBlock 업데이트
        for (UpdateSubBlockRequest subReq : request.getSubBlockRequests()) {
            SubBlock subBlock = mainBlock.getSubBlocks().stream()
                    .filter(sb -> sb.getSubId().equals(subReq.getSubId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("SubBlock not found"));

            if (subReq.getContent() != null) {
                subBlock.setContent(subReq.getContent());
            }
            if (subReq.getStatus() != null) {
                subBlock.setStatus(subReq.getStatus());
            }

            // Cell 업데이트
            for (UpdateCellRequest cellReq : subReq.getCells()) {
                Cell cell = subBlock.getCells().stream()
                        .filter(c -> c.getPosition().equals(cellReq.getPosition()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Cell not found"));

                if (cellReq.getContent() != null) {
                    cell.setContent(cellReq.getContent());
                }
                if (cellReq.getStatus() != null) {
                    cell.setStatus(cellReq.getStatus());
                }
            }
        }
    }
}
