package com.koa.koalamailman.domain.user.dto;

import com.koa.koalamailman.domain.user.repository.AgeGroup;
import com.koa.koalamailman.domain.user.repository.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserProfileRequest(
        @NotNull Gender gender,
        @NotNull AgeGroup ageGroup,
        @NotBlank String job
) {
}