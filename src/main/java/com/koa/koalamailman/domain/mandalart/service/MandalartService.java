package com.koa.koalamailman.domain.mandalart.service;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.MainGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.MandalartDto;
import com.koa.koalamailman.domain.mandalart.dto.SubGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.request.UpdateMandalartRequest;
import com.koa.koalamailman.domain.mandalart.dto.response.MandalartResponse;
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

    private final GoalService goalService;
    private final MandalartRepository mandalartRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public CoreGoalDto createMandalart(Long userId, CoreGoalDto coreGoalDto) {
        MandalartEntity mandalart = findMandalartByUserIdOrCreate(userId);
        return goalService.createAndUpdateGoals(mandalart, coreGoalDto);
    }

    @Transactional
    public MandalartDto createMandalartWithRemind(Long userId, MandalartDto mandalartDto) {
        MandalartEntity mandalart = findMandalartByUserIdOrCreate(userId);
        CoreGoalDto coreGoalDto = goalService.createAndUpdateGoals(mandalart, mandalartDto.coreGoalDto());
        return mandalartDto.from(mandalart, coreGoalDto);
    }

    @Transactional
    public CoreGoalDto updateMandalart(Long mandalartId, CoreGoalDto dto) {
        MandalartEntity mandalart = findMandalartByMandalartId(mandalartId);

        return goalService.createAndUpdateGoals(mandalart, dto);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getMandalartByUserId(Long userId) {
        List<GoalEntity> goals = goalRepository.findGoalsByUserId(userId);
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
                .orElseThrow(() -> new BaseException(MandalartErrorCode.MANDALART_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public MandalartEntity findMandalartByMandalartId(Long mandalartId) {
        return mandalartRepository.findById(mandalartId)
                .orElseThrow(() -> new BaseException(MandalartErrorCode.MANDALART_NOT_FOUND));
    }

}