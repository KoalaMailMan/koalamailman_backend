package com.koa.koalamailman.domain.mandalart.dto.response;

import com.koa.koalamailman.domain.mandalart.dto.MandalartDto;
import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;

public record MandalartResponse(
        Long mandalartId,
        ReminderOption reminderOption,
        CoreGoalResponse coreGoalResponse
) {
    public static MandalartResponse from(MandalartDto mandalartDto) {
        return new MandalartResponse(mandalartDto.mandalartId(), mandalartDto.reminderOption(), CoreGoalResponse.from(mandalartDto.coreGoalDto()));
    }
}
