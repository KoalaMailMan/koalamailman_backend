package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.reminder.domain.ReminderOption;

public record MandalartDto(
        Long mandalartId,
        ReminderOption reminderOption,
        CoreGoalDto coreGoalDto
) {
    public static MandalartDto from(Mandalart mandalart, CoreGoalDto coreGoalDto) {
        return new MandalartDto(mandalart.getId(), mandalart.getReminderOption(), coreGoalDto);
    }
}
