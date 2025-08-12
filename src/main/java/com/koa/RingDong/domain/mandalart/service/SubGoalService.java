package com.koa.RingDong.domain.mandalart.service;

import com.koa.RingDong.domain.mandalart.dto.UpdateSubGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.SubGoalResponse;
import com.koa.RingDong.domain.mandalart.repository.MainGoal;
import com.koa.RingDong.domain.mandalart.repository.SubGoal;
import com.koa.RingDong.domain.mandalart.repository.SubGoalRepository;
import com.koa.RingDong.domain.mandalart.repository.MainGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SubGoalService {

    private final MainGoalRepository mainGoalRepository;
    private final SubGoalRepository subGoalRepository;

    @Transactional
    public Long createSubGoalsForMainGoal(Long mainGoalId) {
        MainGoal mainGoal = mainGoalRepository.findById(mainGoalId)
                .orElseThrow(() -> new IllegalArgumentException("주요 목표를 찾을 수 없습니다. ID: " + mainGoalId));

        List<SubGoal> subGoals = IntStream.range(0, 8)
                .mapToObj(pos -> SubGoal.builder()
                        .mainGoal(mainGoal)
                        .position(pos)
                        .build())
                .toList();
        subGoalRepository.saveAll(subGoals);

        return mainGoalId;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SubGoalResponse getSubGoal(Long subGoalId) {
        SubGoal subGoal = subGoalRepository.findById(subGoalId)
                .orElseThrow(() -> new IllegalArgumentException("세부 목표가 존재하지 않습니다."));

        return SubGoalResponse.builder()
                .subGoalId(subGoal.getSubGoalId())
                .position(subGoal.getPosition())
                .content(subGoal.getContent())
                .status(subGoal.getStatus())
                .build();
    }

    @Transactional
    public void updateSubGoal(Long mainGoalId, Long subGoalId, UpdateSubGoalRequest request) {
        SubGoal subGoal = subGoalRepository.findById(subGoalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세부 목표가 존재하지 않습니다."));

        subGoal.updateSubGoal(request.getContent(), request.getStatus());
    }
}
