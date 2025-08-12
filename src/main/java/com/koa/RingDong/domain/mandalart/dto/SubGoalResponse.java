package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.SubGoal;
import com.koa.RingDong.domain.mandalart.repository.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubGoalResponse {
    private Long subGoalId;
    private Integer position;
    private String content;
    private Status status;

    public static SubGoalResponse from(SubGoal subGoal) {
        return SubGoalResponse.builder()
                .subGoalId(subGoal.getSubGoalId())
                .position(subGoal.getPosition())
                .content(subGoal.getContent())
                .status(subGoal.getStatus())
                .build();
    }
}
