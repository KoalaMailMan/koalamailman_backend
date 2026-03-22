package com.koa.koalamailman.mandalart.dto.response;

import com.koa.koalamailman.mandalart.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.mandalart.repository.entity.Status;

public record SubGoalResponse(
        Long goalId,
        int position,
        String content,
        Status status
) {
    public static SubGoalResponse of(GoalEntity sub) {
        return new SubGoalResponse(sub.getGoalId(), sub.getPosition(), sub.getContent(), sub.getStatus());
    }

    public static SubGoalResponse from(SubGoalDto dto) {
        return new SubGoalResponse(dto.id(), dto.position(), dto.content(), dto.status());
    }
}
