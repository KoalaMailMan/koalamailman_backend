package com.koa.koalamailman.domain.mandalart.dto.request;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.MandalartDto;
import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;

public record UpdateMandalartRequest(
        Long mandalartId,
        ReminderOption reminderOption,
        UpdateCoreGoalRequest updateCoreGoalRequest
) {
}
