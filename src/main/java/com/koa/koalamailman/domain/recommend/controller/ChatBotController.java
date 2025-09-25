package com.koa.koalamailman.domain.recommend.controller;

import com.koa.koalamailman.domain.recommend.service.ChatService;
import com.koa.koalamailman.domain.recommend.service.ProfileSessionService;
import com.koa.koalamailman.domain.recommend.session.ProfileSession;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatBotController {
//    private final ChatService chatService;
//    private final ProfileSessionService profileSessionService;
//
//
//    @PostMapping("/init")
//    public void init(
//            @AuthenticationPrincipal CustomUserDetails userDetails,
//            HttpSession httpSession
//    ) {
//        profileSessionService.initProfileToSession(userDetails.getUserId(), httpSession);
//    }
//
//    @GetMapping(value = "/reference/main", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<String> generationStreamingMainGoal(
//            @RequestParam("category") String category,
//            HttpSession session
//    ) {
//        return chatService.referenceMainGoalByCategory(category, (ProfileSession) session.getAttribute("session:profile:"));
//    }
//
//    @GetMapping(value = "/reference/sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<String> generationStreamingSubGoal(
//            @RequestParam("goal") String mainGoal,
//            HttpSession session
//    ) {
//        return chatService.referenceSubGoalByMainGoal(mainGoal, (ProfileSession) session.getAttribute("session:profile:"));
//    }
}
