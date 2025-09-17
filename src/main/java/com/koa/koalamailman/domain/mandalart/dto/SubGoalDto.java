package com.koa.koalamailman.domain.mandalart.dto;

import com.koa.koalamailman.domain.mandalart.dto.request.UpdateSubGoalRequest;

public record SubGoalDto(
        Long id,
         int position,
         String content
//       Status status
) {
    public static SubGoalDto fromRequest(UpdateSubGoalRequest req) {
        return new SubGoalDto(req.goalId(), req.position(), req.content());
    }

}