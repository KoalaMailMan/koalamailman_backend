package com.koa.koalamailman.domain.recommend.controller;

import com.koa.koalamailman.domain.recommend.controller.docs.RecommendControllerDocs;
import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.domain.recommend.service.RecommendService;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Validated
public class RecommendController implements RecommendControllerDocs {

    private final RecommendService recommendService;

    @Cacheable(value = "childGoalsCache", key = "T(String).format('%s_%d', #parentGoal, #recommendationCount)")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<ChildGoalsResponse> generationSubGoalList(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount
    ) {
        return SuccessResponse.success(
                SuccessCode.GET_RECOMMEND_SUCCESS,
                recommendService.getChildGoalByParentGoal(parentGoal, recommendationCount)
        );
    }

    @GetMapping(value = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generationStreamingChildGoal(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount
    ) {
        return recommendService.streamingChildGoalByParentGoal(parentGoal, recommendationCount);
    }
}
