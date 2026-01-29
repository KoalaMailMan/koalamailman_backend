package com.koa.koalamailman.domain.recommend.controller.docs;

import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Tag(name = "챗봇", description = "챗봇 관련 API입니다.")
public interface ChatControllerDocs {

    @Operation(summary = "챗봇 세션 초기화", description = "챗봇 사용을 위한 세션을 초기화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세션 초기화 성공")
    })
    void init(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(hidden = true)
            HttpSession httpSession
    );

    @Operation(summary = "주요 목표 추천 스트리밍", description = "카테고리 기반으로 주요 목표를 스트리밍 방식으로 추천합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스트리밍 연결 성공",
                    content = @Content(mediaType = "text/event-stream"))
    })
    SuccessResponse<Void> generationStreamingMainGoal(
            @Parameter(description = "추천받을 목표의 카테고리 (예: '운동', '공부')")
            @RequestParam("category") String category,
            @Parameter(hidden = true)
            HttpSession session
    );


    @Operation(summary = "세부 목표 추천 스트리밍", description = "주요 목표에 대한 세부 목표를 스트리밍 방식으로 추천합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스트리밍 연결 성공",
                    content = @Content(mediaType = "text/event-stream"))
    })
    SuccessResponse<Void> generationStreamingSubGoal(
            @Parameter(description = "세부 목표를 추천받을 주요 목표")
            @RequestParam("goal") String mainGoal,
            @Parameter(hidden = true)
            HttpSession session
    );
}