package com.koa.koalamailman.mandalart.service;

import com.koa.koalamailman.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.dto.MainGoalDto;
import com.koa.koalamailman.mandalart.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.repository.GoalRepository;
import com.koa.koalamailman.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.mandalart.repository.entity.GoalLevel;
import com.koa.koalamailman.mandalart.repository.entity.MandalartEntity;
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
    public CoreGoalDto createAndUpdateGoals(MandalartEntity mandalart, CoreGoalDto coreDto) {
        // db에 이미 있는 목표 goalId 별 map
        List<GoalEntity> currentGoals = goalRepository.findGoalsByMandalartId(mandalart.getId());
        Map<Long, GoalEntity> currentGoalsMap = new HashMap<>();
        for (GoalEntity g : currentGoals) {
            currentGoalsMap.put(g.getGoalId(), g);
        }

        // db에 새로 생성되어야 하는 목표 list
        List<GoalEntity> newGoals = new ArrayList<>();

        // create or update된 모든 목표 list
        List<GoalEntity> allGoals = new ArrayList<>();

        // -----CORE-----
        GoalEntity core;
        if (coreDto.id() != null) {
            core = getGoalFromGoalsMapByGoalId(currentGoalsMap, coreDto.id());
            core.updateGoal(coreDto.content(), coreDto.status());
        } else {
            core = GoalEntity.createCoreGoal(mandalart, coreDto.content(), coreDto.status());
            newGoals.add(core);
        }
        allGoals.add(core);

        // ------MAIN-----
        for (MainGoalDto mainDto : coreDto.mains()) {
            GoalEntity main;

            if (mainDto.id() != null) {
                main = getGoalFromGoalsMapByGoalId(currentGoalsMap, mainDto.id());
                main.updateGoal(mainDto.content(), mainDto.status());
            } else {
                main = GoalEntity.createMainGoal(mandalart, mainDto.position(), mainDto.content(), mainDto.status());
                newGoals.add(main);
            }
            allGoals.add(main);

            // -----SUB-----
            for (SubGoalDto subDto : mainDto.subs()) {
                GoalEntity sub;

                if (subDto.id() != null) {
                    sub = getGoalFromGoalsMapByGoalId(currentGoalsMap, subDto.id());
                    sub.updateGoal(subDto.content(), subDto.status());
                } else {
                    sub = GoalEntity.createSubGoal(mandalart, mainDto.position(), subDto.position(), subDto.content(), subDto.status());
                    newGoals.add(sub);
                }
                allGoals.add(sub);
            }
        }

        if (!newGoals.isEmpty()) goalRepository.saveAll(newGoals);

        return CoreGoalDto.fromEntities(allGoals);
    }

    private GoalEntity getGoalFromGoalsMapByGoalId(Map<Long, GoalEntity> goalsMapById, Long goalId) {
        GoalEntity goalEntity = goalsMapById.get(goalId);
        if (goalEntity == null) throw new BusinessException(MandalartErrorCode.GOAL_NOT_FOUND);
        return goalEntity;
    }

    public List<GoalEntity> getCoreAndMainGoalsFromMandalart(MandalartEntity mandalart) {
        return goalRepository.findByMandalartIdAndLevelIn(mandalart.getId(), List.of(GoalLevel.CORE, GoalLevel.MAIN));
    }
}
