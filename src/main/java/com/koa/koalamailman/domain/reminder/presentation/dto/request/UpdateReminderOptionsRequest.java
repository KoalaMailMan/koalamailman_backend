package com.koa.koalamailman.domain.reminder.presentation.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.RemindInterval;
import jakarta.validation.constraints.NotNull;

public record UpdateReminderOptionsRequest(
        @NotNull Long mandalartId,
        @NotNull Boolean reminderEnabled,
        RemindInterval reminderInterval
) {
}
