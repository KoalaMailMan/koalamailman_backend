package com.koa.koalamailman.mandalart.presentation.dto.response;

import com.koa.koalamailman.mandalart.application.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.Status;

public record SubGoalResponse(
        Long goalId,
        int position,
        String content,
        Status status
) {
    public static SubGoalResponse of(Goal sub) {
        return new SubGoalResponse(sub.getGoalId(), sub.getPosition(), sub.getContent(), sub.getStatus());
    }

    public static SubGoalResponse from(SubGoalDto dto) {
        return new SubGoalResponse(dto.id(), dto.position(), dto.content(), dto.status());
    }
}
