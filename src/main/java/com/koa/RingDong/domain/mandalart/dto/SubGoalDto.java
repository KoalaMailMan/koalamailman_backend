package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.dto.request.UpdateSubGoalRequest;

public record SubGoalDto(
        Long id,
         int position,
         String content
//       Status status
) {
    public static SubGoalDto fromRequest(UpdateSubGoalRequest req) {
        return new SubGoalDto(req.subGoalId(), req.position(), req.content());
    }

}