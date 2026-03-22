package com.koa.koalamailman.mandalart.dto;

import com.koa.koalamailman.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.mandalart.repository.entity.ReminderOption;

public record MandalartDto(
        Long mandalartId,
        ReminderOption reminderOption,
        CoreGoalDto coreGoalDto
) {
    public static MandalartDto from(MandalartEntity mandalart, CoreGoalDto coreGoalDto) {
        return new MandalartDto(mandalart.getId(), mandalart.getReminderOption(), coreGoalDto);
    }
}
