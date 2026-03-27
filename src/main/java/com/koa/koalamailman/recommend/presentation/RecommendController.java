package com.koa.koalamailman.recommend.presentation;

import com.koa.koalamailman.recommend.presentation.docs.RecommendControllerDocs;
import com.koa.koalamailman.recommend.presentation.dto.ChildGoalsResponse;
import com.koa.koalamailman.recommend.presentation.dto.StreamingErrorData;
import com.koa.koalamailman.recommend.application.RecommendService;
import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import java.util.List;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.exception.error.RecommendErrorCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<ChildGoalsResponse> generationSubGoalList(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount,
            @RequestParam(value = "ageGroup", required = false) AgeGroup ageGroup,
            @RequestParam(value = "gender", required = false)Gender gender,
            @RequestParam(value = "job", required = false) String job,
            @RequestParam(value = "excludeGoals", required = false) List<String> excludeGoals
            ) {
        return SuccessResponse.success(
                SuccessCode.GET_RECOMMEND_SUCCESS,
                ChildGoalsResponse.from(recommendService.getChildGoalByParentGoal(parentGoal, recommendationCount, ageGroup, gender, job, excludeGoals))
        );
    }

    @GetMapping(value = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> generationStreamingChildGoal(
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount,
            @RequestParam(value = "ageGroup", required = false) AgeGroup ageGroup,
            @RequestParam(value = "gender", required = false) Gender gender,
            @RequestParam(value = "job", required = false) String job,
            @RequestParam(value = "excludeGoals", required = false) List<String> excludeGoals
    ) {
        return recommendService.streamingChildGoalByParentGoal(parentGoal, recommendationCount, ageGroup, gender, job, excludeGoals)
                .map(goal -> ServerSentEvent.builder().data(goal).build())
                .concatWith(Flux.just(ServerSentEvent.builder().event("complete").data("").build()))
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
