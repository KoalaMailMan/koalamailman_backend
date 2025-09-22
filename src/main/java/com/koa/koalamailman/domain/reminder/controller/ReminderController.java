package com.koa.koalamailman.domain.reminder.controller;

import com.koa.koalamailman.domain.reminder.controller.docs.ReminderControllerDocs;
import com.koa.koalamailman.domain.reminder.dto.request.UpdateReminderOptionsRequest;
import com.koa.koalamailman.domain.reminder.service.ReminderService;
import com.koa.koalamailman.global.dto.RequestDataWrapper;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
public class ReminderController implements ReminderControllerDocs {

    private final ReminderService reminderService;

    @PatchMapping
    @Override
    public SuccessResponse<Void> updateReminderOptions(
            CustomUserDetails userDetails,
            @RequestBody @Valid RequestDataWrapper<UpdateReminderOptionsRequest> request) {
        reminderService.updateReminderOption(request.getData());
        return SuccessResponse.success(
                SuccessCode.UPDATE_REMINDER_SUCCESS
        );
    }
}
