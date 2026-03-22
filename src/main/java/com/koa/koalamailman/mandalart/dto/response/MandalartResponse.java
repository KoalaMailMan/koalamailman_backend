package com.koa.koalamailman.mandalart.dto.response;

import com.koa.koalamailman.mandalart.dto.MandalartDto;
import com.koa.koalamailman.mandalart.repository.entity.ReminderOption;

public record MandalartResponse(
        Long mandalartId,
        ReminderOption reminderOption,
        CoreGoalResponse core
) {
    public static MandalartResponse from(MandalartDto mandalartDto) {
        return new MandalartResponse(mandalartDto.mandalartId(), mandalartDto.reminderOption(), CoreGoalResponse.from(mandalartDto.coreGoalDto()));
    }
}
