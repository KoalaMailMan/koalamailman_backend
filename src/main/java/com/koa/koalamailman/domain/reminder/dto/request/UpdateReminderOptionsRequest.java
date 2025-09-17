package com.koa.koalamailman.domain.reminder.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderInterval;
import jakarta.validation.constraints.NotNull;

public record UpdateReminderOptionsRequest(
        @NotNull Long mandalartId,
        @NotNull Boolean reminderEnabled,
        ReminderInterval reminderInterval
) {
}
