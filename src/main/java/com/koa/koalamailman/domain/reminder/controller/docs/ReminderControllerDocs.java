package com.koa.koalamailman.domain.reminder.controller.docs;

import com.koa.koalamailman.domain.reminder.dto.request.UpdateReminderOptionsRequest;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@SecurityRequirement(name = "Authorization")
@Tag(name = "리마인더", description = "리마인더 관련 API입니다.")
public interface ReminderControllerDocs {
    @Operation(summary = "리마인더 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리마인더 수정 성공")
    })
    SuccessResponse<Void> updateReminderOptions(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            final UpdateReminderOptionsRequest request
    );
}
