package com.koa.RingDong.domain.mandalart.controller;

import com.koa.RingDong.domain.mandalart.controller.docs.MandalartControllerDocs;
import com.koa.RingDong.domain.mandalart.service.MandalartService;
import com.koa.RingDong.domain.mandalart.dto.UpdateCoreGoalRequest;
import com.koa.RingDong.global.dto.ApiResponse;
import com.koa.RingDong.domain.mandalart.dto.CoreGoalResponse;
import com.koa.RingDong.global.dto.SuccessResponse;
import com.koa.RingDong.global.exception.SuccessCode;
import com.koa.RingDong.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mandalart")
public class MandalartController implements MandalartControllerDocs {

    private final MandalartService mandalartService;

    @PostMapping()
    @Override
    public SuccessResponse<CoreGoalResponse> creatMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateCoreGoalRequest request
    ){
        return SuccessResponse.success(
                SuccessCode.CREATE_MANDALART_SUCCESS,
                mandalartService.createMandalart(userDetails.getUserId(), request)
        );
    }

    @Operation(summary = "만다라트 전체 9x9 조회")
    @GetMapping()
    public SuccessResponse<CoreGoalResponse> getMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return SuccessResponse.success(
            SuccessCode.GET_MANDALART_SUCCESS,
            mandalartService.getMandalart(userDetails.getUserId())
        );
    }

    @Operation(summary = "만다라트 전체 9x9 수정")
    @PatchMapping()
    public SuccessResponse<CoreGoalResponse> updateMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateCoreGoalRequest request
    ) {
        return SuccessResponse.success(
                SuccessCode.UPDATE_MANDALART_SUCCESS,
                mandalartService.updateMandalart(userDetails.getUserId(), request)
        );
    }
}
