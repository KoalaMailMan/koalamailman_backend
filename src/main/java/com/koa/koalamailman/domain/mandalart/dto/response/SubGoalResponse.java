package com.koa.koalamailman.domain.mandalart.dto.response;

import com.koa.koalamailman.domain.mandalart.dto.SubGoalDto;
import com.koa.koalamailman.domain.mandalart.repository.entity.GoalEntity;

public record SubGoalResponse(
        Long goalId,
        int position,
        String content
) {
    public static SubGoalResponse of(GoalEntity sub) {
        return new SubGoalResponse(sub.getGoalId(), sub.getPosition(), sub.getContent());
    }

    public static SubGoalResponse from(SubGoalDto dto) {
        return new SubGoalResponse(dto.id(), dto.position(), dto.content());
    }
}
