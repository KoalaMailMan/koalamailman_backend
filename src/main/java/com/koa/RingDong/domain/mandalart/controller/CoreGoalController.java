package com.koa.RingDong.domain.mandalart.controller;

import com.koa.RingDong.domain.mandalart.service.CoreGoalService;
import com.koa.RingDong.global.dto.ApiResponse;
import com.koa.RingDong.domain.mandalart.dto.CoreGoalResponse;
import com.koa.RingDong.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core-goal")
public class CoreGoalController {

    private final CoreGoalService coreGoalService;

    @Operation(summary = "만다라트 핵심 목표(정중앙) 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<CoreGoalResponse>> getCoreGoal(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CoreGoalResponse response = coreGoalService.getCoreGoal(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("만다라트 핵심 목표(정중앙) 조회", response));
    }
}
