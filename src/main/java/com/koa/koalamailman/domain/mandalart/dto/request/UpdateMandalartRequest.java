package com.koa.koalamailman.domain.mandalart.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;

public record UpdateMandalartRequest(
        Long mandalartId,
        ReminderOption reminderOption,
        UpdateCoreGoalRequest core
) {
}
