package com.koa.RingDong.domain.mandalart.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateMainGoalRequest (
    Long mainGoalId,
    @NotNull
    @Min(1) @Max(8)
    Integer position,
    String content,
    //Status status,
    @NotNull
    @Size(min = 8, max = 8, message = "subGoal은 8개여야 합니다.")
    List<UpdateSubGoalRequest> subGoals
) {
}
