package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.Status;
import com.koa.RingDong.domain.mandalart.repository.MainGoal;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainGoalResponse {
    private Long mainGoalId;
    private Integer position;
    private String content;
    private Status status;
    private List<SubGoalResponse> subGoals;

    public static MainGoalResponse from(MainGoal mainGoal) {
        return MainGoalResponse.builder()
                .mainGoalId(mainGoal.getMainGoalId())
                .position(mainGoal.getPosition())
                .content(mainGoal.getContent())
                .status(mainGoal.getStatus())
                .subGoals(
                        mainGoal.getSubGoals().stream()
                                .map(SubGoalResponse::from)
                                .toList()
                )
                .build();
    }
}
