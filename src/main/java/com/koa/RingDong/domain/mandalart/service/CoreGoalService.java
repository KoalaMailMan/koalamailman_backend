package com.koa.RingDong.domain.mandalart.service;

import com.koa.RingDong.domain.mandalart.dto.CoreGoalResponse;
import com.koa.RingDong.domain.mandalart.dto.MainGoalResponse;
import com.koa.RingDong.domain.mandalart.repository.entity.CoreGoal;
import com.koa.RingDong.domain.mandalart.repository.entity.MainGoal;
import com.koa.RingDong.domain.mandalart.repository.MainGoalRepository;
import com.koa.RingDong.domain.mandalart.repository.CoreGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CoreGoalService {

    private final CoreGoalRepository coreGoalRepository;
    private final MainGoalRepository mainGoalRepository;

    @Transactional
    public CoreGoal createCoreGoal(Long userId) {
        CoreGoal coreGoal = CoreGoal.builder()
                .userId(userId)
                .build();
        coreGoalRepository.save(coreGoal);

        List<MainGoal> mainGoals = IntStream.range(0, 8)
                .mapToObj(pos -> MainGoal.builder()
                        .coreGoal(coreGoal)
                        .position(pos)
                        .build())
                .toList();
        mainGoalRepository.saveAll(mainGoals);

        return coreGoal;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CoreGoalResponse getCoreGoal(Long userId) {
        CoreGoal coreGoal = coreGoalRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("핵심 목표가 존재하지 않습니다. userId: " + userId));

        List<MainGoalResponse> mainGoals = mainGoalRepository.findByCoreGoal(coreGoal).stream()
                .map(main -> MainGoalResponse.builder()
                        .mainGoalId(main.getMainGoalId())
                        .position(main.getPosition())
                        .content(main.getContent())
                        .status(main.getStatus())
                        .build())
                .toList();

        return CoreGoalResponse.builder()
                .coreGoalId(coreGoal.getCoreGoalId())
                .userId(coreGoal.getUserId())
                .content(coreGoal.getContent())
                .status(coreGoal.getStatus())
                .reminderInterval(coreGoal.getReminderInterval())
                .mainGoals(mainGoals)
                .build();
    }
}
