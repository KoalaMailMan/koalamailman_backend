package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.presentation.dto.request.UpdateSubGoalRequest;
import com.koa.koalamailman.mandalart.domain.Status;

public record SubGoalDto(
        Long id,
         int position,
         String content,
       Status status
) {
    public static SubGoalDto fromRequest(UpdateSubGoalRequest req) {
        return new SubGoalDto(req.goalId(), req.position(), req.content(), req.status());
    }

}