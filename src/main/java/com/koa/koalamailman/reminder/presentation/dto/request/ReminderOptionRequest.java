package com.koa.koalamailman.reminder.presentation.dto.request;

import com.koa.koalamailman.mandalart.domain.RemindInterval;

import java.time.LocalDateTime;

public record ReminderOptionRequest (
        Boolean reminderEnable,
        RemindInterval remindInterval,
        LocalDateTime remindScheduledAt
) {
}
