package com.koa.koalamailman.domain.recommend.session;

import com.koa.koalamailman.domain.user.repository.AgeGroup;
import com.koa.koalamailman.domain.user.repository.Gender;
import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileSession {
    private UUID conversationId;
    private Gender gender;
    private AgeGroup ageGroup;
    private String job;
}
