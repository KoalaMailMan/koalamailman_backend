package com.koa.koalamailman.mandalart.presentation;

import com.koa.koalamailman.mandalart.presentation.docs.MandalartControllerDocs;
import com.koa.koalamailman.mandalart.presentation.dto.request.UpdateMandalartRequest;
import com.koa.koalamailman.mandalart.presentation.dto.response.CoreGoalResponse;
import com.koa.koalamailman.mandalart.presentation.dto.response.MandalartResponse;
import com.koa.koalamailman.mandalart.presentation.dto.request.UpdateCoreGoalRequest;
import com.koa.koalamailman.mandalart.application.MandalartUseCase;
import com.koa.koalamailman.global.dto.RequestDataWrapper;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mandalart")
public class MandalartController implements MandalartControllerDocs {

    private final MandalartUseCase mandalartUseCase;

    @PutMapping
    @Override
    public SuccessResponse<MandalartResponse> creatOrUpdateMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RequestDataWrapper<UpdateMandalartRequest> request
    ) {
        return SuccessResponse.success(
                SuccessCode.CREATE_MANDALART_SUCCESS,
                MandalartResponse.from(mandalartUseCase.createMandalart(userDetails.getUserId(), request.getData().mandalartId(), request.getData().core().coreGoalDto()))
        );
    }

    @GetMapping
    @Override
    public SuccessResponse<MandalartResponse> getMandalartWithReminderOption(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return SuccessResponse.success(
                SuccessCode.GET_MANDALART_SUCCESS,
                MandalartResponse.from(mandalartUseCase.getMandalartWithRemind(userDetails.getUserId()))
        );
    }

    @PatchMapping("/{mandalartId}")
    public SuccessResponse<CoreGoalResponse> updateMandalart(
            @PathVariable(value = "mandalartId") Long mandalartId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateCoreGoalRequest request
    ) {
        return SuccessResponse.success(
                SuccessCode.UPDATE_MANDALART_SUCCESS,
                CoreGoalResponse.from(mandalartUseCase.updateGoals(userDetails.getUserId(), mandalartId, request.coreGoalDto()))
        );
    }
}
