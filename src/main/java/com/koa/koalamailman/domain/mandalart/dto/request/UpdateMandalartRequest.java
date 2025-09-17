package com.koa.koalamailman.domain.mandalart.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;
import jakarta.validation.Valid;

public record UpdateMandalartRequest(
        Long mandalartId,
        ReminderOption reminderOption,
        @Valid
        UpdateCoreGoalRequest core
) {
}
