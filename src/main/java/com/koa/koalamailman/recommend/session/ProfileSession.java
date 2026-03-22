package com.koa.koalamailman.recommend.session;

import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileSession {
    public static final String SESSION_KEY = "session:profile";

    private UUID conversationId;
    private Gender gender;
    private AgeGroup ageGroup;
    private String job;
}
