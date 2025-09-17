package com.koa.koalamailman.domain.reminder.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderInterval;

public record UpdateReminderOptionsRequest(
        Long mandalartId,
        Boolean reminderEnabled,
        ReminderInterval reminderInterval
) {
}
