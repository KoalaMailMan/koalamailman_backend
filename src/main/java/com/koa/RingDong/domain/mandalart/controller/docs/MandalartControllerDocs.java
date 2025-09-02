package com.koa.RingDong.domain.mandalart.controller.docs;

import com.koa.RingDong.domain.mandalart.dto.request.UpdateCoreGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.response.CoreGoalResponse;
import com.koa.RingDong.global.dto.SuccessResponse;
import com.koa.RingDong.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

//@SecurityRequirement(name = "Authorization")
@Tag(name = "만다라트", description = "만다라트 관련 API입니다.")
public interface MandalartControllerDocs {
    @Operation(summary = "만다라트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "만다라트 생성 성공")
    })
    SuccessResponse<CoreGoalResponse> creatMandalart(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            final UpdateCoreGoalRequest request
    );


    @Operation(summary = "만다라트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "만다라트 조회 성공")
    })
    SuccessResponse<CoreGoalResponse> getMandalart(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
        );

    @Operation(summary = "만다라트 수정")
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
