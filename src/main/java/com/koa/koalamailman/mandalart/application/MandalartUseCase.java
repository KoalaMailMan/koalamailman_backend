package com.koa.koalamailman.mandalart.application;

import com.koa.koalamailman.mandalart.application.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.application.dto.MandalartDto;
import com.koa.koalamailman.mandalart.infrastructure.GoalRepository;
import com.koa.koalamailman.mandalart.infrastructure.MandalartRepository;
import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.GoalLevel;
import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.global.exception.error.MandalartErrorCode;
import com.koa.koalamailman.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MandalartUseCase {

    private final GoalService goalService;
    private final MandalartRepository mandalartRepository;
    private final GoalRepository goalRepository;

    @Transactional
    public MandalartDto createMandalart(Long userId, Long mandalartId, CoreGoalDto coreGoalDto) {
        Mandalart mandalart = mandalartId == null
                ? findMandalartByUserIdOrCreate(userId)
                : findMandalartByMandalartId(userId, mandalartId);
        List<Goal> goals =  goalService.createAndUpdateGoals(mandalart, coreGoalDto);
        return MandalartDto.from(mandalart, CoreGoalDto.from(goals));
    }

    @Transactional(readOnly = true)
    public MandalartDto getMandalartWithRemind(Long userId) {
        Mandalart mandalart = findMandalartByUserId(userId);
        return MandalartDto.from(mandalart, getGoalsByMandalartId(mandalart.getId()));
    }

    @Transactional
    public CoreGoalDto updateGoals(Long userId, Long mandalartId, CoreGoalDto coreGoalDto) {
        Mandalart mandalart = findMandalartByMandalartId(userId, mandalartId);
        List<Goal> goals = goalService.createAndUpdateGoals(mandalart, coreGoalDto);
        return CoreGoalDto.from(goals);
    }

    @Transactional(readOnly = true)
    public CoreGoalDto getGoalsByMandalartId(Long mandalartId) {
        List<Goal> goals = goalRepository.findGoalsByMandalartId(mandalartId);
        return CoreGoalDto.from(goals);
    }

    private Mandalart findMandalartByUserIdOrCreate(Long userId) {
        return mandalartRepository.findByUserId(userId)
                .orElseGet(() -> mandalartRepository.save(Mandalart.create(userId)));
    }

    private Mandalart findMandalartByUserId(Long userId) {
        return mandalartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(MandalartErrorCode.MANDALART_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Mandalart findMandalartByMandalartId(Long userId, Long mandalartId) {
        Mandalart mandalart = mandalartRepository.findById(mandalartId)
                .orElseThrow(() -> new BusinessException(MandalartErrorCode.MANDALART_NOT_FOUND));

        if (!Objects.equals(mandalart.getUserId(), userId)) throw new BusinessException(MandalartErrorCode.MANDALART_FORBIDDEN);
        return mandalart;
    }

    @Transactional(readOnly = true)
    public List<Mandalart> findDueMandalarts(LocalDateTime until) {
        return mandalartRepository.findDueReminders(until);
    }

    @Transactional(readOnly = true)
    public List<Goal> getCoreAndMainGoals(Long mandalartId) {
        return goalRepository.findByMandalartIdAndLevelIn(mandalartId, List.of(GoalLevel.CORE, GoalLevel.MAIN));
    }

    @Transactional
    public void rescheduleReminder(Long mandalartId, LocalDateTime nextTime) {
        Mandalart mandalart = mandalartRepository.findById(mandalartId).orElse(null);
        if (mandalart == null) return;
        mandalart.rescheduleReminder(nextTime);
    }
}