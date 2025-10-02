package com.koa.koalamailman.domain.recommend.controller.docs;

import com.koa.koalamailman.domain.recommend.dto.ChildGoalsResponse;
import com.koa.koalamailman.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@SecurityRequirement(name = "Authorization")
@Tag(name = "목표 추천", description = "목표 추천 관련 API입니다.")
public interface RecommendControllerDocs {

    @Operation(summary = "세부(child) 목표 추천", description = "주요(parent) 목표에 대한 세부(child) 목표를 추천합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공"),
            @ApiResponse(responseCode = "204", description = "LLM 응답이 비어있어 추천 내용이 없음")
    })
    SuccessResponse<ChildGoalsResponse> generationSubGoalList(
            @Parameter(description = "주요(parent) 목표")
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @Parameter(description = "추천 받을 목표 갯수")
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount
    );

    @Operation(summary = "세부(child) 목표 추천 SSE 스트리밍", description = "주요(parent) 목표에 대한 세부(child) 목표를 스트리밍 방식으로 추천합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스트리밍 연결 성공",
                    content = @Content(
                            mediaType = "text/event-stream",
                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                    )
            )
    })
    Flux<String> generationStreamingChildGoal(
            @Parameter(description = "주요(parent) 목표")
            @RequestParam("parentGoal") @NotNull String parentGoal,
            @Parameter(description = "추천 받을 목표 갯수")
            @RequestParam("recommendationCount") @NotNull @Max(8) int recommendationCount
    );
}
