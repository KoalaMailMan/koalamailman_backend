package com.koa.koalamailman.user.presentation.dto.request;

import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserProfileRequest(
        @NotNull Gender gender,
        @NotNull AgeGroup ageGroup,
        @NotBlank String job
) {
}