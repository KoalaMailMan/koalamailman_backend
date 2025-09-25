package com.koa.koalamailman.domain.recommend.controller;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.domain.mandalart.dto.response.MandalartResponse;
import com.koa.koalamailman.domain.recommend.controller.docs.RecommendControllerDocs;
import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.domain.recommend.service.RecommendService;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController implements RecommendControllerDocs {

    private final RecommendService recommendService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SuccessResponse<ChildGoalsResponse> generationSubGoalList(
            @RequestParam("parentGoal") String parentGoal,
            @RequestParam("recommendationCount") int recommendationCount
    ) {
        // recommendationCount 갯수 제한
        return SuccessResponse.success(
                SuccessCode.CREATE_MANDALART_SUCCESS,
                recommendService.getChildGoalByParentGoal(parentGoal, recommendationCount)
        );
    }

    @GetMapping(value = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generationStreamingChildGoal(
            @RequestParam("parentGoal") String parentGoal,
            @RequestParam("recommendationCount") int recommendationCount
    ) {
        return recommendService.streamingChildGoalByParentGoal(parentGoal, recommendationCount);
    }
}
