package com.koa.koalamailman.domain.mandalart.controller;

import com.koa.koalamailman.domain.mandalart.controller.docs.MandalartControllerDocs;
import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.request.UpdateMandalartRequest;
import com.koa.koalamailman.domain.mandalart.dto.response.CoreGoalResponse;
import com.koa.koalamailman.domain.mandalart.dto.response.MandalartResponse;
import com.koa.koalamailman.domain.mandalart.service.MandalartService;
import com.koa.koalamailman.domain.mandalart.dto.request.UpdateCoreGoalRequest;
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

    private final MandalartService mandalartService;

    @PutMapping
    @Override
    public SuccessResponse<MandalartResponse> creatOrUpdateMandalart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RequestDataWrapper<UpdateMandalartRequest> request
    ) {
        return SuccessResponse.success(
                SuccessCode.CREATE_MANDALART_SUCCESS,
                MandalartResponse.from(mandalartService.createMandalart(userDetails.getUserId(), request.getData().mandalartId(), CoreGoalDto.fromRequest(request.getData().core())))
        );
    }

    @GetMapping
    @Override
    public SuccessResponse<MandalartResponse> getMandalartWithReminderOption(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return SuccessResponse.success(
                SuccessCode.GET_MANDALART_SUCCESS,
                MandalartResponse.from(mandalartService.getMandalartWithRemind(userDetails.getUserId()))
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
                CoreGoalResponse.from(mandalartService.updateMandalart(mandalartId, CoreGoalDto.fromRequest(request)))
        );
    }
}
