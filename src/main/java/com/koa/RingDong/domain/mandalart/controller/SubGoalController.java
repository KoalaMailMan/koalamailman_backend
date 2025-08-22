package com.koa.RingDong.domain.mandalart.controller;

import com.koa.RingDong.domain.mandalart.service.SubGoalService;
import com.koa.RingDong.domain.mandalart.dto.UpdateSubGoalRequest;
import com.koa.RingDong.global.dto.ApiResponse;
import com.koa.RingDong.domain.mandalart.dto.SubGoalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main-goal/{mainGoalId}/sub-goal/{subGoalId}")
public class SubGoalController {

    private final SubGoalService subGoalService;

    @Operation(summary = "만다라트 세부 목표 조회")
    @GetMapping()
    public ResponseEntity<ApiResponse<SubGoalResponse>> getSubGoal(
            @PathVariable Long subGoalId
    ) {
        SubGoalResponse response = subGoalService.getSubGoal(subGoalId);
        return ResponseEntity.ok(ApiResponse.success("만다라트 세부 목표 조회", response));
    }

    @Operation(summary = "만다라트 세부 목표 수정")
    @PatchMapping()
    public ResponseEntity<ApiResponse<String>> updateSubGoal(
            @PathVariable Long mainGoalId,
            @PathVariable Long subGoalId,
            @RequestBody @Valid UpdateSubGoalRequest request
    ) {
        subGoalService.updateSubGoal(mainGoalId, subGoalId, request);
        return ResponseEntity.ok(ApiResponse.success("만다라트 세부 목표 수정", null));
    }
}
