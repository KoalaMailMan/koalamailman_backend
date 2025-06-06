package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.CreateSubBlockRequest;
import com.koa.RingDong.dto.request.UpdateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateSubBlockRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.dto.response.SubBlockResponse;
import com.koa.RingDong.entity.SubBlock;
import com.koa.RingDong.service.SubBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/{mainId}/sub")
public class SubBlockController {

    private final SubBlockService subBlockService;

    @Operation(summary = "만다라트 세부 목표 생성")
    @PostMapping("/{subId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createSubBlockWithCells(
            @PathVariable("mainId") Long mainId,
            @PathVariable("subId") Long subId
    ) {
        Long subBlockId = subBlockService.createSub(mainId, subId);
        return ResponseEntity.ok(ApiResponse.success("서브 블럭 및 셀 생성 성공", Map.of("subBlockId", subBlockId)));
    }

    @Operation(summary = "sub id별 sub block 조회")
    @GetMapping("/{subId}")
    public ResponseEntity<ApiResponse<SubBlockResponse>> getSubBlockWithCells(
            @PathVariable Long subId
    ) {
        SubBlockResponse response = subBlockService.getSubBlock(subId);
        return ResponseEntity.ok(ApiResponse.success("서브 블럭 조회 성공", response));
    }

    @Operation(summary = "만다라트 세부 목표 수정")
    @PatchMapping("/{subId}")
    public ResponseEntity<ApiResponse<String>> updateSub(
            @PathVariable Long mainId,
            @PathVariable Long subId,
            @RequestBody @Valid UpdateSubBlockRequest request
    ) {
        subBlockService.updateSubBlock(mainId, subId, request);
        return ResponseEntity.ok(ApiResponse.success("서브 블럭 및 셀 업데이트 성공", null));
    }

}