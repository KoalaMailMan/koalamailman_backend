package com.koa.RingDong.domain.user.dto;

import com.koa.RingDong.domain.user.repository.AgeGroup;
import com.koa.RingDong.domain.user.repository.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserProfileRequest(
        @NotNull Gender gender,
        @NotNull AgeGroup ageGroup,
        @NotBlank String job
) {
}