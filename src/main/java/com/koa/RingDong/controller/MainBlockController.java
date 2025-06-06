package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.CreateMainBlockRequest;
import com.koa.RingDong.dto.request.UpdateMainBlockRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.dto.response.MainBlockResponse;
import com.koa.RingDong.security.CustomUserDetails;
import com.koa.RingDong.service.MainBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainBlockController {

    private final MainBlockService mainBlockService;

    @Operation(summary = "만다라트 핵심 목표 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createMainBlock(
            @RequestBody @Valid CreateMainBlockRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long mainId = mainBlockService.createMainBlock(request, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("만다라트 핵심 목표 생성 성공", Map.of("mainId", mainId)));
    }

    @Operation(summary = "만다라트 전체 조회")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<MainBlockResponse>> getMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        MainBlockResponse response = mainBlockService.getAllBlock(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("전체 조회 성공", response));
    }

    @Operation(summary = "만다라트 핵심 목표 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<MainBlockResponse>> getMainWithSubBlocks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MainBlockResponse response = mainBlockService.getMainBlock(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("조회 성공", response));
    }

    @Operation(summary = "만다라트 핵심 목표 수정")
    @PatchMapping("/{mainId}")
    public ResponseEntity<ApiResponse<String>> updateMain(
            @PathVariable Long mainId,
            @RequestBody @Valid UpdateMainBlockRequest request
            ) {
        mainBlockService.updateMain(mainId, request);
        return ResponseEntity.ok(ApiResponse.success("메인 블럭 및 서브 블럭 업데이트 성공", null));
    }
}
