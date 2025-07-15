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

@Service
@RequiredArgsConstructor
@Slf4j
public class MandalartService {

    private final MainBlockRepository mainBlockRepository;
    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public MainBlockResponse createMandalart(Long userId, UpdateMainBlockRequest request) {
        log.info("[만다라트] 생성 createMandalart");

        // 1. MainBlock 생성
        MainBlock mainBlock = MainBlock.builder()
                .userId(userId)
                .content(request.getContent())
                .reminderInterval(request.getReminderInterval())
                .nextScheduledTime(reminderTimeProvider.generateRandomTime(request.getReminderInterval()))
                .build();

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
        log.info("[만다라트] 생성 createMandalart" + saved.toString());

        return mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElseThrow(() -> new IllegalStateException("Mandalart creation failed"));
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public MainBlockResponse getMandalart(Long userId) {
        MainBlockResponse response = mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElse(null);

        log.info("[만다라트] 조회 getMandalart");
        return response;
    }

    @Transactional
    public MainBlockResponse updateMandalart(Long userId, UpdateMainBlockRequest request) {
        Optional<MainBlock> optional = mainBlockRepository.findFullMandalartByUserId(userId);

        if (optional.isEmpty()) {
            log.info("[만다라트] 생성 updateMandalart - Mandalart 없는 경우 create");
            return createMandalart(userId, request); // 여기선 바로 응답 리턴
        }

        log.info("[만다라트] 수정 updateMandalart - Mandalart 있는 경우 update");
        MainBlock mainBlock = optional.get();

        // MainBlock 업데이트
        mainBlock.updateMainBlockField(request, reminderTimeProvider.generateRandomTime(request.getReminderInterval()));

        // SubBlock 업데이트
        for (UpdateSubBlockRequest subReq : request.getSubBlockRequests()) {
            SubBlock subBlock = mainBlock.getSubBlocks().stream()
                    .filter(sb -> sb.getSubId().equals(subReq.getSubId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("SubBlock not found"));
            subBlock.updateSubBlockField(subReq.getContent(), subReq.getStatus());

            // Cell 업데이트
            for (UpdateCellRequest cellReq : subReq.getCells()) {
                Cell cell = subBlock.getCells().stream()
                        .filter(c -> c.getCellId().equals(cellReq.getCellId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Cell not found with ID: " + cellReq.getCellId()));

                cell.updateCellField(subBlock.getContent(), subReq.getStatus());
            }
        }
        return mainBlockRepository.findFullMandalartByUserId(userId)
                .map(MainBlockResponse::from)
                .orElseThrow(() -> new IllegalStateException("Mandalart creation failed"));
    }
}
