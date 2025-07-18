package com.koa.RingDong.domain.mandalart.controller;


import com.koa.RingDong.domain.mandalart.service.MainBlockService;
import com.koa.RingDong.global.dto.ApiResponse;
import com.koa.RingDong.domain.mandalart.dto.MainBlockResponse;
import com.koa.RingDong.global.token.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainAreaController {

    private final MainBlockService mainBlockService;

    @Operation(summary = "만다라트 핵심 3x3 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<MainBlockResponse>> getMainArea(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MainBlockResponse response = mainBlockService.getMainArea(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("만다라트 핵심 3x3 조회", response));
    }
}
