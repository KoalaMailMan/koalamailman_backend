package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.CreateMainBlockRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.service.MainBlockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainBlockController {

    private final MainBlockService mainBlockService;

    @Operation(summary = "만다라트 핵심 목표 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createMainBlock(
            @RequestBody @Valid CreateMainBlockRequest request
    ) {
        Long mainId = mainBlockService.createMainBlock(request);
        return ResponseEntity.ok(ApiResponse.success("만다라트 핵심 목표 생성 성공", Map.of("mainId", mainId)));
    }
}
