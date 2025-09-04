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
    @Size(min = 0, max = 8)
    List<UpdateSubGoalRequest> subGoals
) {
}
