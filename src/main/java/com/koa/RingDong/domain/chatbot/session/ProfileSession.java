package com.koa.RingDong.domain.chatbot.session;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileSession {
    private UUID conversationId;
    private String gender;
    private String ageGroup;
    private String job;
}
