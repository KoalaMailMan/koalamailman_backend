package com.koa.RingDong.domain.chatbot.session;

import com.koa.RingDong.domain.user.repository.AgeGroup;
import com.koa.RingDong.domain.user.repository.Gender;
import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileSession {
    private UUID conversationId;
    private Gender gender;
    private AgeGroup ageGroup;
    private String job;
}
