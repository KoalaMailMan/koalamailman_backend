package com.koa.koalamailman.domain.reminder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MandalartEmailMessage(
        @NotNull
        String from,
        @NotNull
        String to,
        @NotNull
        String subject, // 메일 제목
        String username,
        String[][] grid,
        String tip,
        String logoUrl, // 로고 이미지
        String heroUrl, // 배너 이미지
        String ctaUrl
        ) {
}
