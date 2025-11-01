package com.koa.RingDong.domain.reminder.dto;

import com.koa.RingDong.domain.mandalart.repository.entity.ReminderInterval;

import java.time.LocalDateTime;

public record ReminderDto(
        Long mandalartId,
        Boolean reminderEnable,
        ReminderInterval remindInterval,
        LocalDateTime remindScheduledAt
) {
}
