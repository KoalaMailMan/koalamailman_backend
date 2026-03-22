package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.GoalLevel;
import com.koa.koalamailman.mandalart.domain.Status;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;

import java.util.*;

public record CoreGoalDto(
        Long id,
        String content,
        Status status,
        List<MainGoalDto> mains
) {
    public static CoreGoalDto from(List<Goal> goals) {
        if (goals == null || goals.isEmpty()) {
            throw new IllegalArgumentException("Goals cannot be null or empty");
        }

        // 1. 레벨별로 분류
        Map<GoalLevel, List<Goal>> goalsMapByLevel = new EnumMap<>(GoalLevel.class);
        for (Goal goal : goals) {
            goalsMapByLevel.computeIfAbsent(goal.getLevel(), k -> new ArrayList<>()).add(goal);
        }

        // 2. SubGoalDto parentPosition(mainPosition) 별
        List<Goal> subGoals = goalsMapByLevel.getOrDefault(GoalLevel.SUB, new ArrayList<>());
        Map<Integer, List<SubGoalDto>> subGoalsMapByParentPosition = new HashMap<>();
        for (Goal sub : subGoals) {
            subGoalsMapByParentPosition.computeIfAbsent(sub.getParentPosition(), k -> new ArrayList<>())
                    .add(new SubGoalDto(
                            sub.getGoalId(),
                            sub.getPosition(),
                            sub.getContent(),
                            sub.getStatus()
                        )
                    );
        }

        // 3. MainGoalDto
        List<Goal> mainGoals = goalsMapByLevel.getOrDefault(GoalLevel.MAIN, new ArrayList<>());
        List<MainGoalDto> mainDtos = new ArrayList<>();
        for (Goal main : mainGoals) {
            mainDtos.add(new MainGoalDto(
                    main.getGoalId(),
                    main.getPosition(),
                    main.getContent(),
                    main.getStatus(),
                    subGoalsMapByParentPosition.getOrDefault(main.getPosition(), new ArrayList<>())
            ));
        }

        // 4. CoreGoalDto
        List<Goal> coreGoals = goalsMapByLevel.getOrDefault(GoalLevel.CORE, new ArrayList<>());
        if (coreGoals.isEmpty() || coreGoals.size() > 1) {
            throw new BusinessException(MandalartErrorCode.GOAL_NOT_FOUND);
        }
        Goal core = coreGoals.get(0);

        return new CoreGoalDto(core.getGoalId(), core.getContent(), core.getStatus(), mainDtos);
    }

}
