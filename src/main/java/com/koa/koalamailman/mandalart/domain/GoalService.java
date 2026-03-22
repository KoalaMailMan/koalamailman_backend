package com.koa.koalamailman.mandalart.domain;

import com.koa.koalamailman.mandalart.application.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.application.dto.MainGoalDto;
import com.koa.koalamailman.mandalart.application.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.infrastructure.GoalRepository;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;

    @Transactional
    public List<Goal> createAndUpdateGoals(Mandalart mandalart, CoreGoalDto coreDto) {
        // db에 이미 있는 목표 goalId 별 map
        List<Goal> currentGoals = goalRepository.findGoalsByMandalartId(mandalart.getId());
        Map<Long, Goal> currentGoalsMap = new HashMap<>();
        for (Goal g : currentGoals) {
            currentGoalsMap.put(g.getGoalId(), g);
        }

        // db에 새로 생성되어야 하는 목표 list
        List<Goal> newGoals = new ArrayList<>();

        // create or update된 모든 목표 list
        List<Goal> allGoals = new ArrayList<>();

        // -----CORE-----
        Goal core;
        if (coreDto.id() != null) {
            core = getGoalFromGoalsMapByGoalId(currentGoalsMap, coreDto.id());
            core.updateGoal(coreDto.content(), coreDto.status());
        } else {
            core = Goal.createCoreGoal(mandalart, coreDto.content(), coreDto.status());
            newGoals.add(core);
        }
        allGoals.add(core);

        // ------MAIN-----
        for (MainGoalDto mainDto : coreDto.mains()) {
            Goal main;

            if (mainDto.id() != null) {
                main = getGoalFromGoalsMapByGoalId(currentGoalsMap, mainDto.id());
                main.updateGoal(mainDto.content(), mainDto.status());
            } else {
                main = Goal.createMainGoal(mandalart, mainDto.position(), mainDto.content(), mainDto.status());
                newGoals.add(main);
            }
            allGoals.add(main);

            // -----SUB-----
            for (SubGoalDto subDto : mainDto.subs()) {
                Goal sub;

                if (subDto.id() != null) {
                    sub = getGoalFromGoalsMapByGoalId(currentGoalsMap, subDto.id());
                    sub.updateGoal(subDto.content(), subDto.status());
                } else {
                    sub = Goal.createSubGoal(mandalart, mainDto.position(), subDto.position(), subDto.content(), subDto.status());
                    newGoals.add(sub);
                }
                allGoals.add(sub);
            }
        }

        if (!newGoals.isEmpty()) goalRepository.saveAll(newGoals);

        return allGoals;
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
