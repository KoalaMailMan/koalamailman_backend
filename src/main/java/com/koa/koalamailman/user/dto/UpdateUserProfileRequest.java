package com.koa.koalamailman.user.dto;

import com.koa.koalamailman.user.repository.AgeGroup;
import com.koa.koalamailman.user.repository.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserProfileRequest(
        @NotNull Gender gender,
        @NotNull AgeGroup ageGroup,
        @NotBlank String job
) {
}