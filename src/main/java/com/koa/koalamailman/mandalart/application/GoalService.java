package com.koa.koalamailman.mandalart.application;

import com.koa.koalamailman.mandalart.application.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.application.dto.MainGoalDto;
import com.koa.koalamailman.mandalart.application.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.GoalLevel;
import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.mandalart.domain.Status;
import com.koa.koalamailman.mandalart.infrastructure.GoalRepository;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;

    public List<Goal> createAndUpdateGoals(Mandalart mandalart, CoreGoalDto coreDto) {
        Map<Long, Goal> currentGoalsMap = goalRepository.findGoalsByMandalartId(mandalart.getId())
                .stream()
                .collect(Collectors.toMap(Goal::getGoalId, g -> g));

        List<Goal> newGoals = new ArrayList<>();
        List<Goal> allGoals = new ArrayList<>();

        // CORE
        allGoals.add(resolveGoal(currentGoalsMap, coreDto.id(),
                () -> Goal.createCoreGoal(mandalart, coreDto.content(), coreDto.status()),
                coreDto.content(), coreDto.status(), newGoals));

        // MAIN + SUB
        for (MainGoalDto mainDto : coreDto.mains()) {
            allGoals.add(resolveGoal(currentGoalsMap, mainDto.id(),
                    () -> Goal.createMainGoal(mandalart, mainDto.position(), mainDto.content(), mainDto.status()),
                    mainDto.content(), mainDto.status(), newGoals));

            for (SubGoalDto subDto : mainDto.subs()) {
                allGoals.add(resolveGoal(currentGoalsMap, subDto.id(),
                        () -> Goal.createSubGoal(mandalart, mainDto.position(), subDto.position(), subDto.content(), subDto.status()),
                        subDto.content(), subDto.status(), newGoals));
            }
        }

        if (!newGoals.isEmpty()) goalRepository.saveAll(newGoals);

        return allGoals;
    }

    private Goal resolveGoal(Map<Long, Goal> map, Long id, Supplier<Goal> creator,
                             String content, Status status, List<Goal> newGoals) {
        Goal goal;
        if (id != null) {
            goal = getGoalFromGoalsMapByGoalId(map, id);
            goal.updateGoal(content, status);
        } else {
            goal = creator.get();
            newGoals.add(goal);
        }
        return goal;
    }

    private Goal getGoalFromGoalsMapByGoalId(Map<Long, Goal> goalsMapById, Long goalId) {
        Goal goal = goalsMapById.get(goalId);
        if (goal == null) throw new BusinessException(MandalartErrorCode.GOAL_NOT_FOUND);
        return goal;
    }

    public List<Goal> getCoreAndMainGoalsFromMandalart(Mandalart mandalart) {
        return goalRepository.findByMandalartIdAndLevelIn(mandalart.getId(), List.of(GoalLevel.CORE, GoalLevel.MAIN));
    }
}
