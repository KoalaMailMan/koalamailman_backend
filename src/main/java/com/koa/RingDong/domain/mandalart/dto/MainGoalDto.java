package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.dto.request.UpdateMainGoalRequest;
import java.util.List;

public record MainGoalDto (
        Long id,
        int position,
        String content,
//        Status status,
        List<SubGoalDto> subs
) {
    public static MainGoalDto fromRequest(UpdateMainGoalRequest req) {
        List<SubGoalDto> subDtos = (req.subGoalRequests() == null) ? List.of()
                : req.subGoalRequests().stream().map(SubGoalDto::fromRequest).toList();
        return new MainGoalDto(req.mainGoalId(), req.position(), req.content(), subDtos);
    }
}