package com.koa.koalamailman.domain.recommend.controller;

import com.koa.koalamailman.domain.recommend.controller.docs.RecommendControllerDocs;
import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.domain.recommend.dto.StreamingErrorData;
import com.koa.koalamailman.domain.recommend.service.RecommendService;
import com.koa.koalamailman.domain.user.repository.AgeGroup;
import com.koa.koalamailman.domain.user.repository.Gender;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Validated
public class RecommendController implements RecommendControllerDocs {

    private final RecommendService recommendService;

    @Cacheable(value = "childGoalsCache", key = "T(String).format('%s_%d', #parentGoal, #recommendationCount)")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<ChildGoalsResponse> generationSubGoalList(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount,
            @RequestParam(value = "ageGroup", required = false) AgeGroup ageGroup,
            @RequestParam(value = "gender", required = false)Gender gender,
            @RequestParam(value = "job", required = false) String job
            ) {
        return SuccessResponse.success(
                SuccessCode.GET_RECOMMEND_SUCCESS,
                recommendService.getChildGoalByParentGoal(parentGoal, recommendationCount, ageGroup, gender, job)
        );
    }

    @GetMapping(value = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> generationStreamingChildGoal(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount,
            @RequestParam(value = "ageGroup", required = false) AgeGroup ageGroup,
            @RequestParam(value = "gender", required = false) Gender gender,
            @RequestParam(value = "job", required = false) String job
    ) {
        return recommendService.streamingChildGoalByParentGoal(parentGoal, recommendationCount, ageGroup, gender, job)
                .map(goal -> ServerSentEvent.builder().data(goal).build())
                .concatWith(Flux.just(ServerSentEvent.builder().event("complete").build()))
                .doOnComplete(() -> log.info("[목표 추천] 모든 streaming 데이터 전송 완료"))
                .onErrorResume(e -> {
                    log.error("[목표 추천] 스트리밍 에러 발생: {}", e.getClass().getSimpleName(), e);
                    return Flux.just(
                            ServerSentEvent.<Object>builder()
                                    .event("error")
                                    .data(StreamingErrorData.from(RecommendErrorCode.fromException(e)))
                                    .build()
                    );
                });
    }
}
