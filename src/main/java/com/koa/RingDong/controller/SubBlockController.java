package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.CreateSubBlockRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.service.SubBlockService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/{mainId}/sub")
public class SubBlockController {

    private final SubBlockService subBlockService;

    @Operation(summary = "만다라트 세부 목표 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createSubBlockWithCells(
            @PathVariable Long mainId,
            @RequestBody @Valid CreateSubBlockRequest request
    ) {
        Long subBlockId = subBlockService.createSubBlock(mainId, request);
        return ResponseEntity.ok(ApiResponse.success("서브 블럭 및 셀 생성 성공", Map.of("subBlockId", subBlockId)));
    }
}