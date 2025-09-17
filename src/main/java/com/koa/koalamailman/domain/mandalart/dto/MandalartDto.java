package com.koa.koalamailman.domain.mandalart.dto;

import com.koa.koalamailman.domain.mandalart.dto.request.UpdateMandalartRequest;
import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;

public record MandalartDto(
        Long mandalartId,
        ReminderOption reminderOption,
        CoreGoalDto coreGoalDto
) {
    public static MandalartDto fromRequest(UpdateMandalartRequest request) {
        return new MandalartDto(request.mandalartId(), request.reminderOption(), CoreGoalDto.fromRequest(request.core()));
    }

    public static MandalartDto from(MandalartEntity mandalart, CoreGoalDto coreGoalDto) {
        return new MandalartDto(mandalart.getId(), mandalart.getReminderOption(), coreGoalDto);
    }
}
