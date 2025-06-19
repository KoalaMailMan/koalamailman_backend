package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.UpdateCellRequest;
import com.koa.RingDong.dto.request.UpdateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateSubBlockRequest;
import com.koa.RingDong.dto.response.MainBlockResponse;
import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.provider.ReminderTimeProvider;
import com.koa.RingDong.repository.MainBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MandalartService {

    private final MainBlockRepository mainBlockRepository;
    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public MainBlockResponse createMandalart(Long userId, UpdateMainBlockRequest request) {
        log.info("createMandalart");

        // 1. MainBlock 생성
        MainBlock mainBlock = MainBlock.builder()
                .userId(userId)
                .content(request.getContent())
                .reminderInterval(request.getReminderInterval())
                .build();

        mainBlock.setNextScheduledTime(
                reminderTimeProvider.generateRandomTime(request.getReminderInterval())
        );

        Set<SubBlock> subBlocks = new HashSet<>();

        // 2. SubBlock 생성 (요청 기반으로)
        for (UpdateSubBlockRequest subReq : request.getSubBlockRequests()) {
            SubBlock subBlock = SubBlock.builder()
                    .mainBlock(mainBlock)
                    .position(subReq.getPosition())
                    .content(subReq.getContent())
                    .status(subReq.getStatus())
                    .build();

            Set<Cell> cells = new HashSet<>();

            for (UpdateCellRequest cellReq : subReq.getCells()) {
                Cell cell = Cell.builder()
                        .subBlock(subBlock)
                        .position(cellReq.getPosition())
                        .content(cellReq.getContent())
                        .status(cellReq.getStatus())
                        .build();
                cells.add(cell);
            }

            subBlock.getCells().addAll(cells);
            subBlocks.add(subBlock);
        }

        mainBlock.getSubBlocks().addAll(subBlocks);

        // 3. 저장
        MainBlock saved = mainBlockRepository.save(mainBlock);
        log.info("createMandalart" + saved.toString());

        return mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElseThrow(() -> new IllegalStateException("Mandalart creation failed"));
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getMandalart(Long userId) {
        MainBlockResponse response = mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElse(null);

        log.info("getMandalart");
        return response;
    }

    @Transactional
    public MainBlockResponse updateMandalart(Long userId, UpdateMainBlockRequest request) {
        Optional<MainBlock> optional = mainBlockRepository.findFullMandalartByUserId(userId);
        log.info("updateMandalart - " + request);

        if (optional.isEmpty()) {
            log.info("updateMandalart - Mandalart 없는 경우 create");
            return createMandalart(userId, request); // 여기선 바로 응답 리턴
        }

        log.info("updateMandalart - Mandalart 있는 경우 update");
        MainBlock mainBlock = optional.get();

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
        mainBlock.setNextScheduledTime(reminderTimeProvider.generateRandomTime(request.getReminderInterval()));

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
                        .filter(c -> c.getCellId().equals(cellReq.getCellId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Cell not found with ID: " + cellReq.getCellId()));

                if (cellReq.getContent() != null) {
                    cell.setContent(cellReq.getContent());
                }
                if (cellReq.getStatus() != null) {
                    cell.setStatus(cellReq.getStatus());
                }
            }
        }
        return mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElseThrow(() -> new IllegalStateException("Mandalart creation failed"));
    }
}
