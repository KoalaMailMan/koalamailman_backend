package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.entity.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public record UpdateMainGoalRequest (
    Long mainGoalId,
    @NotNull
    Integer position,
    String content,
    Status status,
    @NotNull
    @Size(min = 8, max = 8, message = "subGoal은 8개여야 합니다.")
    List<UpdateSubGoalRequest> subGoals
) {
}
