package com.koa.koalamailman.reminder.presentation.dto.request;

import com.koa.koalamailman.mandalart.repository.entity.RemindInterval;
import jakarta.validation.constraints.NotNull;

public record UpdateReminderOptionsRequest(
        @NotNull Long mandalartId,
        @NotNull Boolean reminderEnabled,
        RemindInterval reminderInterval
) {
}
