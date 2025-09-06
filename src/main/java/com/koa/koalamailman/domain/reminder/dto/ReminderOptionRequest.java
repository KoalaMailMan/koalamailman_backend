package com.koa.koalamailman.domain.reminder.dto;

import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderInterval;

import java.time.LocalDateTime;

public record ReminderOptionRequest (
        Boolean reminderEnable,
        ReminderInterval remindInterval,
        LocalDateTime remindScheduledAt
) {
}
