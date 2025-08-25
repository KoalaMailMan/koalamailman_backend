package com.koa.RingDong.domain.user.controller.docs;

import com.koa.RingDong.domain.user.dto.UpdateUserProfileRequest;
import com.koa.RingDong.domain.user.dto.UserResponse;
import com.koa.RingDong.global.dto.ErrorResponse;
import com.koa.RingDong.global.dto.SuccessResponse;
import com.koa.RingDong.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@SecurityRequirement(name = "Authorization")
@Tag(name = "사용자", description = "사용자 관련 API입니다.")
public interface UserControllerDocs {
    @Operation(summary = "유저 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<UserResponse> getUserInfo(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "유저 프로필 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 프로필 수정 성공"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse updateUserProfile(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            final UpdateUserProfileRequest request
    );
}
