package com.koa.RingDong.service;

import com.koa.RingDong.dto.request.*;
import com.koa.RingDong.dto.response.CellResponse;
import com.koa.RingDong.dto.response.SubBlockResponse;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.repository.SubBlockRepository;
import com.koa.RingDong.repository.CellRepository;
import com.koa.RingDong.repository.MainBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SubBlockResponse getSubBlock(Long subBlockId) {
        SubBlock subBlock = subBlockRepository.findById(subBlockId)
                .orElseThrow(() -> new IllegalArgumentException("서브 블럭이 존재하지 않습니다."));

        List<CellResponse> cells = cellRepository.findBySubBlock(subBlock).stream()
                .map(cell -> CellResponse.builder()
                        .position(cell.getPosition())
                        .content(cell.getContent())
                        .build())
                .toList();

        return SubBlockResponse.builder()
                .subId(subBlock.getSubId())
                .position(subBlock.getPosition())
                .content(subBlock.getContent())
                .status(subBlock.getStatus())
                .cellResponses(cells)
                .build();
    }

    @Transactional
    public void updateSubBlock(Long mainId, Long subId, UpdateSubBlockRequest req) {
        SubBlock subBlock = subBlockRepository.findById(subId)
                .orElseThrow(() -> new IllegalArgumentException("해당 서브 블럭이 존재하지 않습니다."));

        subBlock.setContent(req.getContent());
        subBlock.setStatus(req.getStatus());

        List<Cell> cells = cellRepository.findBySubBlock(subBlock);

        Map<Integer, Cell> cellMap = cells.stream()
                .collect(Collectors.toMap(Cell::getPosition, Function.identity()));

        for (UpdateCellRequest cellReq : req.getCells()) {
            Cell cell = cellMap.get(cellReq.getPosition());
            if (cell != null) {
                cell.setContent(cellReq.getContent());
                cell.setStatus(cellReq.getStatus());
            } else {
                throw new IllegalArgumentException("해당 position의 셀이 존재하지 않습니다: " + cellReq.getPosition());
            }
        }
    }
}