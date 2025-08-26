package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.entity.CoreGoal;
import com.koa.RingDong.domain.mandalart.repository.entity.ReminderInterval;
import com.koa.RingDong.domain.mandalart.repository.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CoreGoalResponse {
    private Long coreGoalId;
    private Long userId;
    private String content;
    private Status status;
    private ReminderInterval reminderInterval;
    private List<MainGoalResponse> mainGoals;

    public static CoreGoalResponse from(CoreGoal coreGoal) {
        return CoreGoalResponse.builder()
                .coreGoalId(coreGoal.getCoreGoalId())
                .userId(coreGoal.getUserId())
                .content(coreGoal.getContent())
                .reminderInterval(coreGoal.getReminderInterval())
                .status(coreGoal.getStatus())
                .mainGoals(
                        coreGoal.getMainGoals().stream()
                                .map(MainGoalResponse::from)
                                .toList()
                )
                .build();
    }
}
