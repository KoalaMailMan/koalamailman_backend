package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.UpdateSubBlockRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.dto.response.SubBlockResponse;
import com.koa.RingDong.service.SubBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/{mainId}/sub/{subId}")
public class SubAreaController {

    private final SubBlockService subBlockService;

    @Operation(summary = "만다라트 세부 3x3 조회")
    @GetMapping()
    public ResponseEntity<ApiResponse<SubBlockResponse>> getSubArea(
            @PathVariable Long subId
    ) {
        SubBlockResponse response = subBlockService.getSubArea(subId);
        return ResponseEntity.ok(ApiResponse.success("만다라트 세부 3x3 조회", response));
    }

    @Operation(summary = "만다라트 세부 3x3 수정")
    @PatchMapping()
    public ResponseEntity<ApiResponse<String>> updateSubArea(
            @PathVariable Long mainId,
            @PathVariable Long subId,
            @RequestBody @Valid UpdateSubBlockRequest request
    ) {
        subBlockService.updateSubArea(mainId, subId, request);
        return ResponseEntity.ok(ApiResponse.success("만다라트 세부 3x3 수정", null));
    }
}