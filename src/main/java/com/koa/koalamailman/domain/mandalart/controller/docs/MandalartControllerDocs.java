package com.koa.koalamailman.domain.mandalart.controller.docs;

import com.koa.koalamailman.domain.mandalart.dto.request.UpdateCoreGoalRequest;
import com.koa.koalamailman.domain.mandalart.dto.request.UpdateMandalartRequest;
import com.koa.koalamailman.domain.mandalart.dto.response.CoreGoalResponse;
import com.koa.koalamailman.domain.mandalart.dto.response.MandalartResponse;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@SecurityRequirement(name = "Authorization")
@Tag(name = "만다라트", description = "만다라트 관련 API입니다.")
public interface MandalartControllerDocs {
    @Operation(summary = "만다라트 생성 및 수정", description = "대시 보드 화면 3x3 만다라트 저장 버튼")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "만다라트 생성 성공")
    })
    SuccessResponse<MandalartResponse> creatOrUpdateMandalart(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            final UpdateMandalartRequest request
    );

    @Operation(summary = "만다라트 조회", description = "대시 보드 화면 진입 시 만다라트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "만다라트 조회 성공")
    })
    SuccessResponse<MandalartResponse> getMandalartWithReminderOption(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "[보류] 만다라트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "만다라트 수정 성공")
    })
    SuccessResponse<CoreGoalResponse> updateMandalart(
            @PathVariable(value = "mandalartId") Long mandalartId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            final UpdateCoreGoalRequest request
        );
}
