package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.dto.request.UpdateCoreGoalRequest;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalEntity;
import com.koa.RingDong.domain.mandalart.repository.entity.GoalLevel;
import jakarta.validation.constraints.Size;

import java.util.*;

public record CoreGoalDto(
        Long id,
        String content,
        List<MainGoalDto> mains
) {
    public static CoreGoalDto fromRequest(UpdateCoreGoalRequest req) {
        List<MainGoalDto> mains = (req.mainGoalRequests() == null) ? List.of()
                : req.mainGoalRequests().stream()
                .map(MainGoalDto::fromRequest)
                .toList();
        return new CoreGoalDto(req.coreGoalId(), req.content(), mains);
    }

    public static CoreGoalDto fromEntities(List<GoalEntity> goals) {
        if (goals == null || goals.isEmpty()) {
            throw new IllegalArgumentException("Goals cannot be null or empty");
        }

        // 1. 레벨별로 분류하고 position으로 정렬
        Map<GoalLevel, List<GoalEntity>> goalsByLevel = new EnumMap<>(GoalLevel.class);
        for (GoalEntity goal : goals) {
            goalsByLevel.computeIfAbsent(goal.getLevel(), k -> new ArrayList<>()).add(goal);
        }

        for (List<GoalEntity> list : goalsByLevel.values()) {
            list.sort(Comparator.comparingInt(GoalEntity::getPosition));
        }

        // 2. CORE 목표 찾기
        List<GoalEntity> coreGoals = goalsByLevel.getOrDefault(GoalLevel.CORE, new ArrayList<>());
        if (coreGoals.isEmpty()) {
            throw new IllegalStateException("CORE goal not found");
        }
        GoalEntity core = coreGoals.get(0);

        // 3. CORE의 MAIN 목표들 찾기
        List<GoalEntity> allMains = goalsByLevel.getOrDefault(GoalLevel.MAIN, new ArrayList<>());
        List<GoalEntity> coreMains = new ArrayList<>();
        for (GoalEntity main : allMains) {
            if (Objects.equals(main.getParentId(), core.getGoalId())) {
                coreMains.add(main);
            }
        }

        // 4. SUB 목표들을 MAIN별로 그룹화
        List<GoalEntity> allSubs = goalsByLevel.getOrDefault(GoalLevel.SUB, new ArrayList<>());
        Map<Long, List<SubGoalDto>> subsByMainId = new HashMap<>();

        for (GoalEntity sub : allSubs) {
            Long mainId = sub.getParentId();
            subsByMainId.computeIfAbsent(mainId, k -> new ArrayList<>())
                    .add(new SubGoalDto(sub.getGoalId(), sub.getPosition(), sub.getContent()));
        }

        // 5. MAIN DTO 생성
        List<MainGoalDto> mainDtos = new ArrayList<>();
        for (GoalEntity main : coreMains) {
            List<SubGoalDto> subs = subsByMainId.getOrDefault(main.getGoalId(), new ArrayList<>());
            mainDtos.add(new MainGoalDto(
                    main.getGoalId(),
                    main.getPosition(),
                    main.getContent(),
                    subs
            ));
        }

        return new CoreGoalDto(core.getGoalId(), core.getContent(), mainDtos);
    }

}
