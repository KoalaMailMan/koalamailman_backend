package com.koa.RingDong.domain.mandalart.service;

import com.koa.RingDong.domain.mandalart.dto.UpdateSubGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateCoreGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateMainGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.CoreGoalResponse;
import com.koa.RingDong.domain.mandalart.repository.SubGoal;
import com.koa.RingDong.domain.mandalart.repository.CoreGoal;
import com.koa.RingDong.domain.mandalart.repository.MainGoal;
import com.koa.RingDong.domain.reminder.provider.ReminderTimeProvider;
import com.koa.RingDong.domain.mandalart.repository.CoreGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MandalartService {

    private final CoreGoalRepository coreGoalRepository;
    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public CoreGoalResponse createMandalart(Long userId, UpdateCoreGoalRequest request) {
        log.info("[만다라트] 생성 시작 - userId: {}", userId);

        CoreGoal coreGoal = createCoreGoal(userId, request);
        createMainGoalsWithSubGoals(coreGoal, request.getMainGoalRequests());
        
        CoreGoal saved = coreGoalRepository.save(coreGoal);
        log.info("[만다라트] 생성 완료 - coreGoalId: {}", saved.getCoreGoalId());

        return coreGoalRepository.findCoreGoalWithMainGoalsByUserId(userId)
                .map(CoreGoalResponse::from)
                .orElseThrow(() -> new IllegalStateException("만다라트 생성에 실패했습니다."));
    }

    private CoreGoal createCoreGoal(Long userId, UpdateCoreGoalRequest request) {
        return CoreGoal.builder()
                .userId(userId)
                .content(request.getContent())
                .reminderInterval(request.getReminderInterval())
                .nextScheduledTime(reminderTimeProvider.generateRandomTime(request.getReminderInterval()))
                .build();
    }

    private void createMainGoalsWithSubGoals(CoreGoal coreGoal, List<UpdateMainGoalRequest> mainGoalRequests) {
        Set<MainGoal> mainGoals = new HashSet<>();

        for (UpdateMainGoalRequest mainGoalRequest : mainGoalRequests) {
            MainGoal mainGoal = createMainGoal(coreGoal, mainGoalRequest);
            createSubGoals(mainGoal, mainGoalRequest.getSubGoals());
            mainGoals.add(mainGoal);
        }

        coreGoal.getMainGoals().addAll(mainGoals);
    }

    private MainGoal createMainGoal(CoreGoal coreGoal, UpdateMainGoalRequest mainGoalRequest) {
        return MainGoal.builder()
                .coreGoal(coreGoal)
                .position(mainGoalRequest.getPosition())
                .content(mainGoalRequest.getContent())
                .status(mainGoalRequest.getStatus())
                .build();
    }

    private void createSubGoals(MainGoal mainGoal, List<UpdateSubGoalRequest> subGoalRequests) {
        Set<SubGoal> subGoals = new HashSet<>();

        for (UpdateSubGoalRequest subGoalRequest : subGoalRequests) {
            SubGoal subGoal = SubGoal.builder()
                    .mainGoal(mainGoal)
                    .position(subGoalRequest.getPosition())
                    .content(subGoalRequest.getContent())
                    .status(subGoalRequest.getStatus())
                    .build();
            subGoals.add(subGoal);
        }

        mainGoal.getSubGoals().addAll(subGoals);
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CoreGoalResponse getMandalart(Long userId) {
        CoreGoalResponse response = coreGoalRepository.findCoreGoalWithMainGoalsByUserId(userId)
                .map(CoreGoalResponse::from)
                .orElse(null);

        log.info("[만다라트] 조회 완료 - userId: {}", userId);
        return response;
    }

    @Transactional
    public CoreGoalResponse updateMandalart(Long userId, UpdateCoreGoalRequest request) {
        Optional<CoreGoal> optional = coreGoalRepository.findCoreGoalWithMainGoalsByUserId(userId);

        if (optional.isEmpty()) {
            log.info("[만다라트] 수정 - 기존 데이터 없음, 새로 생성 - userId: {}", userId);
            return createMandalart(userId, request);
        }

        log.info("[만다라트] 수정 시작 - userId: {}", userId);
        CoreGoal coreGoal = optional.get();

        updateCoreGoal(coreGoal, request);
        updateMainGoals(coreGoal, request.getMainGoalRequests());
        
        log.info("[만다라트] 수정 완료 - userId: {}", userId);
        
        return coreGoalRepository.findCoreGoalWithMainGoalsByUserId(userId)
                .map(CoreGoalResponse::from)
                .orElseThrow(() -> new IllegalStateException("만다라트 수정에 실패했습니다."));
    }

    private void updateCoreGoal(CoreGoal coreGoal, UpdateCoreGoalRequest request) {
        coreGoal.updateCoreGoal(request, reminderTimeProvider.generateRandomTime(request.getReminderInterval()));
    }

    private void updateMainGoals(CoreGoal coreGoal, List<UpdateMainGoalRequest> mainGoalRequests) {
        for (UpdateMainGoalRequest mainGoalRequest : mainGoalRequests) {
            MainGoal mainGoal = findMainGoalById(coreGoal, mainGoalRequest.getMainGoalId());
            mainGoal.updateMainGoal(mainGoalRequest.getContent(), mainGoalRequest.getStatus());
            updateSubGoals(mainGoal, mainGoalRequest.getSubGoals());
        }
    }

    private MainGoal findMainGoalById(CoreGoal coreGoal, Long mainGoalId) {
        return coreGoal.getMainGoals().stream()
                .filter(mg -> mg.getMainGoalId().equals(mainGoalId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("주요 목표를 찾을 수 없습니다. ID: " + mainGoalId));
    }

    private void updateSubGoals(MainGoal mainGoal, List<UpdateSubGoalRequest> subGoalRequests) {
        for (UpdateSubGoalRequest subGoalRequest : subGoalRequests) {
            SubGoal subGoal = findSubGoalById(mainGoal, subGoalRequest.getSubGoalId());
            subGoal.updateSubGoal(subGoalRequest.getContent(), subGoalRequest.getStatus());
        }
    }

    private SubGoal findSubGoalById(MainGoal mainGoal, Long subGoalId) {
        return mainGoal.getSubGoals().stream()
                .filter(sg -> sg.getSubGoalId().equals(subGoalId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("세부 목표를 찾을 수 없습니다. ID: " + subGoalId));
    }
}
