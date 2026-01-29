package com.koa.koalamailman.domain.reminder.presentation;

import com.koa.koalamailman.domain.reminder.application.usecase.UpdateReminderOptionUseCase;
import com.koa.koalamailman.domain.reminder.presentation.docs.ReminderControllerDocs;
import com.koa.koalamailman.domain.reminder.presentation.dto.request.UpdateReminderOptionsRequest;
import com.koa.koalamailman.global.dto.RequestDataWrapper;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
public class ReminderController implements ReminderControllerDocs {

    private final UpdateReminderOptionUseCase updateReminderOption;

    @PatchMapping
    @Override
    public SuccessResponse<Void> updateReminderOptions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RequestDataWrapper<UpdateReminderOptionsRequest> request) {
        updateReminderOption.updateReminderOption(userDetails.getUserId(), request.getData());
        return SuccessResponse.success(
                SuccessCode.UPDATE_REMINDER_SUCCESS
        );
    }
}
