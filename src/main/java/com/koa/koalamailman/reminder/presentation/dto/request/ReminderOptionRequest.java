package com.koa.koalamailman.reminder.presentation.dto.request;

import com.koa.koalamailman.reminder.domain.RemindInterval;

import java.time.LocalDateTime;

public record ReminderOptionRequest (
        Boolean reminderEnable,
        RemindInterval remindInterval,
        LocalDateTime remindScheduledAt
) {
}
