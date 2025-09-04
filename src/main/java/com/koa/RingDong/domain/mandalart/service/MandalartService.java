package com.koa.RingDong.domain.mandalart.service;

import com.koa.RingDong.domain.mandalart.dto.CoreGoalDto;
import com.koa.RingDong.domain.mandalart.dto.MainGoalDto;
import com.koa.RingDong.domain.mandalart.dto.SubGoalDto;
import com.koa.RingDong.domain.mandalart.repository.GoalRepository;
import com.koa.RingDong.domain.mandalart.repository.MandalartRepository;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalEntity;
import com.koa.RingDong.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.RingDong.global.exception.ErrorCode;
import com.koa.RingDong.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MandalartService {

    private final MandalartRepository mandalartRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public CoreGoalDto createMandalart(Long userId, CoreGoalDto coreGoalDto) {
        MandalartEntity mandalart = findMandalartOrCreate(userId);
        return saveGoals(mandalart.getId(), coreGoalDto);
    }

    @Transactional
    public CoreGoalDto updateMandalart(Long userId, CoreGoalDto dto) {
        MandalartEntity mandalart = findMandalartOrNotFound(userId);
        return saveGoals(mandalart.getId(), dto);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getMandalart(Long userId) {
        MandalartEntity mandalart = findMandalartOrNotFound(userId);
        List<GoalEntity> goals = goalRepository.findAllByMandalartId(mandalart.getId());
        return CoreGoalDto.fromEntities(goals);
    }

    private MandalartEntity findMandalartOrCreate(Long userId) {
        return mandalartRepository.findByUserId(userId)
                .orElseGet(() -> mandalartRepository.save(MandalartEntity.create(userId)));
    }

    private MandalartEntity findMandalartOrNotFound(Long userId) {
        MandalartEntity mandalart = mandalartRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.MANDALART_NOT_FOUND));

        if (!mandalart.getUserId().equals(userId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }
        return mandalart;
    }

    private CoreGoalDto saveGoals(Long mandalartId, CoreGoalDto coreDto) {
        Map<Long, GoalEntity> existingGoalsById = goalRepository.findAllByMandalartId(mandalartId)
                .stream()
                .collect(Collectors.toMap(GoalEntity::getGoalId, goal -> goal));

        List<GoalEntity> pendingGoalsToSave = new ArrayList<>();
        upsertCoreHierarchy(mandalartId, coreDto, existingGoalsById, pendingGoalsToSave);

        if (!pendingGoalsToSave.isEmpty()) {
            try {
                goalRepository.saveAll(pendingGoalsToSave);
            } catch (DataIntegrityViolationException e) {
                throw new BaseException(ErrorCode.DUPLICATE_GOAL_POSITION);
            }
        }
        List<GoalEntity> allGoals = new ArrayList<>(existingGoalsById.values());
        allGoals.addAll(pendingGoalsToSave);

        return CoreGoalDto.fromEntities(allGoals);
    }

    private GoalEntity getGoalByIdOrNotFound(Map<Long, GoalEntity> goalsById, Long goalId) {
        GoalEntity goalEntity = goalsById.get(goalId);
        if (goalEntity == null) throw new BaseException(ErrorCode.GOAL_NOT_FOUND);
        return goalEntity;
    }

    private void upsertCoreHierarchy(Long mandalartId, CoreGoalDto coreDto, Map<Long, GoalEntity> existingGoalsById, List<GoalEntity> goalsToSave) {

        if (coreDto.id() != null) {
            GoalEntity core = getGoalByIdOrNotFound(existingGoalsById, coreDto.id());
            core.updateGoalInfo(coreDto.content());
        } else {
            goalsToSave.add(GoalEntity.createCoreGoal(mandalartId, coreDto.content()));
        }
        upsertMainHierarchy(mandalartId, coreDto.mains(), existingGoalsById, goalsToSave);
    }

    private void upsertMainHierarchy(Long mandalartId, List<MainGoalDto> mainDtos, Map<Long, GoalEntity> existingGoals, List<GoalEntity> goalsToSave) {
        Set<Integer> mainPositions = new HashSet<>();
        for (MainGoalDto mainDto : mainDtos) {
            if (!mainPositions.add(mainDto.position())) {
                throw new BaseException(ErrorCode.DUPLICATE_GOAL_POSITION);
            }

            GoalEntity main;
            if (mainDto.id() != null) {
                main = getGoalByIdOrNotFound(existingGoals, mainDto.id());
                main.updateGoalInfo(mainDto.content());
            } else {
                main = GoalEntity.createMainGoal(mandalartId, mainDto.position(), mainDto.content());
                goalsToSave.add(main);
            }
            upsertSub(mandalartId, mainDto.subs(), main.getPosition(), existingGoals, goalsToSave);
        }
    }

    private void upsertSub(Long mandalartId, List<SubGoalDto> subDtos, Integer mainPosition, Map<Long, GoalEntity> existingGoals, List<GoalEntity> goalsToSave) {
        Set<Integer> subPositions = new HashSet<>();
        for (SubGoalDto subDto : subDtos) {
            if (!subPositions.add(subDto.position())) {
                throw new BaseException(ErrorCode.DUPLICATE_GOAL_POSITION);
            }

            if (subDto.id() != null) {
                GoalEntity sub = getGoalByIdOrNotFound(existingGoals, subDto.id());
                sub.updateGoalInfo(subDto.content());
            } else {
                goalsToSave.add(GoalEntity.createSubGoal(mandalartId, mainPosition, subDto.position(), subDto.content()));
            }
        }
    }
}