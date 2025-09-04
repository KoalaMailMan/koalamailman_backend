package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.dto.request.UpdateCoreGoalRequest;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalEntity;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalLevel;
import com.koa.RingDong.global.exception.BaseException;
import com.koa.RingDong.global.exception.ErrorCode;

import java.util.*;

public record CoreGoalDto(
        Long id,
        String content,
        List<MainGoalDto> mains
) {
    public static CoreGoalDto fromRequest(UpdateCoreGoalRequest req) {
        List<MainGoalDto> mains = (req.mainGoalRequests() == null) ? List.of()
                : req.mainGoalRequests().stream()
                .map(MainGoalDto::fromRequest)
                .toList();
        return new CoreGoalDto(req.coreGoalId(), req.content(), mains);
    }

    public static CoreGoalDto fromEntities(List<GoalEntity> goals) {
        if (goals == null || goals.isEmpty()) {
            throw new IllegalArgumentException("Goals cannot be null or empty");
        }

        // 1. 레벨별로 분류
        Map<GoalLevel, List<GoalEntity>> goalsMapByLevel = new EnumMap<>(GoalLevel.class);
        for (GoalEntity goal : goals) {
            goalsMapByLevel.computeIfAbsent(goal.getLevel(), k -> new ArrayList<>()).add(goal);
        }

        // 2. SubGoalDto parentPosition(mainPosition) 별
        List<GoalEntity> subGoals = goalsMapByLevel.getOrDefault(GoalLevel.SUB, new ArrayList<>());
        Map<Integer, List<SubGoalDto>> subGoalsMapByParentPosition = new HashMap<>();
        for (GoalEntity sub : subGoals) {
            subGoalsMapByParentPosition.computeIfAbsent(sub.getParentPosition(), k -> new ArrayList<>())
                    .add(new SubGoalDto(
                                    sub.getGoalId(),
                                    sub.getPosition(),
                                    sub.getContent()
                            )
                    );
        }

        // 3. MainGoalDto
        List<GoalEntity> mainGoals = goalsMapByLevel.getOrDefault(GoalLevel.MAIN, new ArrayList<>());
        List<MainGoalDto> mainDtos = new ArrayList<>();
        for (GoalEntity main : mainGoals) {
            mainDtos.add(new MainGoalDto(
                    main.getGoalId(),
                    main.getPosition(),
                    main.getContent(),
                    subGoalsMapByParentPosition.get(main.getPosition())
            ));
        }

        // 4. CoreGoalDto
        List<GoalEntity> coreGoals = goalsMapByLevel.getOrDefault(GoalLevel.CORE, new ArrayList<>());
        if (coreGoals.isEmpty() || coreGoals.size() > 1) {
            throw new BaseException(ErrorCode.GOAL_NOT_FOUND);
        }
        GoalEntity core = coreGoals.get(0);

        return new CoreGoalDto(core.getGoalId(), core.getContent(), mainDtos);
    }

}
