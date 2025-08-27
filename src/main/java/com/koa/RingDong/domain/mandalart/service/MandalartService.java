package com.koa.RingDong.domain.mandalart.service;
import com.koa.RingDong.domain.mandalart.dto.CoreGoalDto;
import com.koa.RingDong.domain.mandalart.dto.MainGoalDto;
import com.koa.RingDong.domain.mandalart.dto.SubGoalDto;
import com.koa.RingDong.domain.mandalart.repository.GoalRepository;
import com.koa.RingDong.domain.mandalart.repository.MandalartRepository;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalEntity;
import com.koa.RingDong.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.RingDong.global.exception.ErrorCode;
import com.koa.RingDong.global.exception.model.NotFoundException;
import com.koa.RingDong.global.exception.model.UnauthorizedException;
import com.koa.RingDong.global.exception.model.DuplicateGoalPositionException;
import lombok.RequiredArgsConstructor;
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
        MandalartEntity mandalart = mandalartRepository.findByUserId(userId)
                .orElseGet(() -> mandalartRepository.save(MandalartEntity.create(userId)));
        return saveGoals(mandalart.getId(), coreGoalDto);
    }

    @Transactional
    public CoreGoalDto updateMandalart(Long userId, CoreGoalDto dto) {
        MandalartEntity mandalart = findMandalartAndValidate(userId);
        return saveGoals(mandalart.getId(), dto);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getMandalart(Long userId) {
        MandalartEntity mandalart = findMandalartAndValidate(userId);
        List<GoalEntity> goals = goalRepository.findAllByMandalartId(mandalart.getId());
        return CoreGoalDto.fromEntities(goals);
    }

    private MandalartEntity findMandalartAndValidate(Long userId) {
        MandalartEntity mandalart = mandalartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MANDALART_NOT_FOUND));

        if (!mandalart.getUserId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
        return mandalart;
    }

    private CoreGoalDto saveGoals(Long mandalartId, CoreGoalDto dto) {
        Map<Long, GoalEntity> existingGoals = goalRepository.findAllByMandalartId(mandalartId)
                .stream()
                .collect(Collectors.toMap(GoalEntity::getGoalId, goal -> goal));

        List<GoalEntity> goalsToSave = new ArrayList<>();

        processCoreGoal(mandalartId, dto, existingGoals, goalsToSave);
        goalRepository.saveAll(goalsToSave);

        List<GoalEntity> allGoals = new ArrayList<>(existingGoals.values());
        allGoals.addAll(goalsToSave);

        return CoreGoalDto.fromEntities(allGoals);
    }

    private void processCoreGoal(Long mandalartId, CoreGoalDto dto, Map<Long, GoalEntity> existingGoals, List<GoalEntity> goalsToSave) {
        GoalEntity core;
        if (dto.id() != null) {
            core = existingGoals.get(dto.id());
            if (core == null) {
                throw new NotFoundException(ErrorCode.GOAL_NOT_FOUND);
            }
            core.updateGoalInfo(dto.content());
        } else {
            core = GoalEntity.createCoreGoal(mandalartId, dto.content());
            goalsToSave.add(core);
        }
        processMainGoals(mandalartId, dto.mains(), core.getGoalId(), existingGoals, goalsToSave);
    }

    private void processMainGoals(Long mandalartId, List<MainGoalDto> mainDtos, Long coreGoalId, Map<Long, GoalEntity> existingGoals, List<GoalEntity> goalsToSave) {
        Set<Integer> mainPositions = new HashSet<>();
        for (MainGoalDto mainDto : mainDtos) {
            if (!mainPositions.add(mainDto.position())) {
                throw new DuplicateGoalPositionException(ErrorCode.DUPLICATE_GOAL_POSITION);
            }

            GoalEntity main;
            if (mainDto.id() != null) {
                main = existingGoals.get(mainDto.id());
                if (main == null) {
                    throw new NotFoundException(ErrorCode.GOAL_NOT_FOUND);
                }
                main.updateGoalInfo(mainDto.content());
            } else {
                main = GoalEntity.createMainGoal(mandalartId, coreGoalId, mainDto.position(), mainDto.content());
                goalsToSave.add(main);
            }
            processSubGoals(mandalartId, mainDto.subs(), main.getGoalId(), existingGoals, goalsToSave);
        }
    }

    private void processSubGoals(Long mandalartId, List<SubGoalDto> subDtos, Long mainGoalId, Map<Long, GoalEntity> existingGoals, List<GoalEntity> goalsToSave) {
        Set<Integer> subPositions = new HashSet<>();
        for (SubGoalDto subDto : subDtos) {
            if (!subPositions.add(subDto.position())) {
                throw new DuplicateGoalPositionException(ErrorCode.DUPLICATE_GOAL_POSITION);
            }

            if (subDto.id() != null) {
                GoalEntity sub = existingGoals.get(subDto.id());
                if (sub == null) {
                    throw new NotFoundException(ErrorCode.GOAL_NOT_FOUND);
                }
                sub.updateGoalInfo(subDto.content());
            } else {
                GoalEntity sub = GoalEntity.createSubGoal(mandalartId, mainGoalId, subDto.position(), subDto.content());
                goalsToSave.add(sub);
            }
        }
    }
}