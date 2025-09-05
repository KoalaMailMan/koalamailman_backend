package com.koa.koalamailman.domain.mandalart.service;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.MainGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.SubGoalDto;
import com.koa.koalamailman.domain.mandalart.repository.GoalRepository;
import com.koa.koalamailman.domain.mandalart.repository.MandalartRepository;
import com.koa.koalamailman.domain.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;
import com.koa.koalamailman.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MandalartService {

    private final MandalartRepository mandalartRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public CoreGoalDto createMandalart(Long userId, CoreGoalDto coreGoalDto) {
        MandalartEntity mandalart = mandalartRepository.findByUserId(userId)
                .orElseGet(() -> mandalartRepository.save(MandalartEntity.create(userId)));

        return createAndUpdateGoals(mandalart, coreGoalDto);
    }

    @Transactional
    public CoreGoalDto updateMandalart(Long mandalartId, CoreGoalDto dto) {
        MandalartEntity mandalart = mandalartRepository.findById(mandalartId)
                .orElseThrow(() -> new BaseException(MandalartErrorCode.MANDALART_NOT_FOUND));

        return createAndUpdateGoals(mandalart, dto);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getMandalart(Long userId) {
        List<GoalEntity> goals = goalRepository.findGoalsByUserId(userId);
        return CoreGoalDto.fromEntities(goals);
    }

    private CoreGoalDto createAndUpdateGoals(MandalartEntity mandalart, CoreGoalDto coreDto) {
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
            core.updateGoalInfo(coreDto.content());
        } else {
            core = GoalEntity.createCoreGoal(mandalart, coreDto.content());
            newGoals.add(core);
        }
        allGoals.add(core);

        // ------MAIN-----
        for (MainGoalDto mainDto : coreDto.mains()) {
            GoalEntity main;

            if (mainDto.id() != null) {
                main = getGoalFromGoalsMapByGoalId(currentGoalsMap, mainDto.id());
                main.updateGoalInfo(mainDto.content());
            } else {
                main = GoalEntity.createMainGoal(mandalart, mainDto.position(), mainDto.content());
                newGoals.add(main);
            }
            allGoals.add(main);

            // -----SUB-----
            for (SubGoalDto subDto : mainDto.subs()) {
                GoalEntity sub;

                if (subDto.id() != null) {
                    sub = getGoalFromGoalsMapByGoalId(currentGoalsMap, subDto.id());
                    sub.updateGoalInfo(subDto.content());
                } else {
                    sub = GoalEntity.createSubGoal(mandalart, mainDto.position(), subDto.position(), subDto.content());
                    newGoals.add(sub);
                }
                allGoals.add(sub);
            }
        }

        if (!newGoals.isEmpty()) {
            try {
                goalRepository.saveAll(newGoals);
            } catch (DataIntegrityViolationException e) {
                throw new BaseException(MandalartErrorCode.DUPLICATE_GOAL_POSITION);
            }
        }
        return CoreGoalDto.fromEntities(allGoals);
    }

    private GoalEntity getGoalFromGoalsMapByGoalId(Map<Long, GoalEntity> goalsMapById, Long goalId) {
        GoalEntity goalEntity = goalsMapById.get(goalId);
        if (goalEntity == null) throw new BaseException(MandalartErrorCode.GOAL_NOT_FOUND);
        return goalEntity;
    }
}