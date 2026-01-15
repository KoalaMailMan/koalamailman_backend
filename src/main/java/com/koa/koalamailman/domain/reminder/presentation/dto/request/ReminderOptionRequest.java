package com.koa.koalamailman.domain.reminder.presentation.dto.request;

import com.koa.koalamailman.domain.mandalart.repository.entity.RemindInterval;

import java.time.LocalDateTime;

public record ReminderOptionRequest (
        Boolean reminderEnable,
        RemindInterval remindInterval,
        LocalDateTime remindScheduledAt
) {
}
