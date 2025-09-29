package com.koa.koalamailman.domain.mandalart.service;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.MandalartDto;
import com.koa.koalamailman.domain.mandalart.repository.GoalRepository;
import com.koa.koalamailman.domain.mandalart.repository.MandalartRepository;
import com.koa.koalamailman.domain.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;
import com.koa.koalamailman.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MandalartService {

    private final GoalService goalService;
    private final MandalartRepository mandalartRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public MandalartDto createMandalart(Long userId, Long mandalartId, CoreGoalDto coreGoalDto) {
        MandalartEntity mandalart;

        if (mandalartId == null) mandalart = findMandalartByUserIdOrCreate(userId);
        else mandalart = findMandalartByMandalartId(mandalartId);

        CoreGoalDto core =  goalService.createAndUpdateGoals(mandalart, coreGoalDto);
        return MandalartDto.from(mandalart, core);
    }

    @Transactional
    public MandalartDto createMandalartWithRemind(Long userId, MandalartDto mandalartDto) {
        MandalartEntity mandalart = findMandalartByUserIdOrCreate(userId);
        CoreGoalDto coreGoalDto = goalService.createAndUpdateGoals(mandalart, mandalartDto.coreGoalDto());
        return mandalartDto.from(mandalart, coreGoalDto);
    }

    @Transactional(readOnly = true)
    public MandalartDto getMandalartWithRemind(Long userId) {
        MandalartEntity mandalart = findMandalartByUserId(userId);
        return MandalartDto.from(mandalart, getMandalartByMandalartId(mandalart.getId()));
    }

    @Transactional
    public CoreGoalDto updateMandalart(Long mandalartId, CoreGoalDto dto) {
        MandalartEntity mandalart = findMandalartByMandalartId(mandalartId);

        return goalService.createAndUpdateGoals(mandalart, dto);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getMandalartByMandalartId(Long mandalartId) {
        List<GoalEntity> goals = goalRepository.findGoalsByMandalartId(mandalartId);
        return CoreGoalDto.fromEntities(goals);
    }

    @Transactional
    public MandalartEntity findMandalartByUserIdOrCreate(Long userId) {
        return mandalartRepository.findByUserId(userId)
                .orElseGet(() -> mandalartRepository.save(MandalartEntity.create(userId)));
    }

    @Transactional(readOnly = true)
    public MandalartEntity findMandalartByUserId(Long userId) {
        return mandalartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(MandalartErrorCode.MANDALART_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public MandalartEntity findMandalartByMandalartId(Long mandalartId) {
        return mandalartRepository.findById(mandalartId)
                .orElseThrow(() -> new BusinessException(MandalartErrorCode.MANDALART_NOT_FOUND));
    }
}