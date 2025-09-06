package com.koa.koalamailman.domain.user.controller;

import com.koa.koalamailman.domain.user.controller.docs.UserControllerDocs;
import com.koa.koalamailman.domain.user.dto.UpdateUserProfileRequest;
import com.koa.koalamailman.domain.user.service.UserService;
import com.koa.koalamailman.domain.user.dto.UserResponse;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserControllerDocs {
    private final UserService userService;

    @GetMapping
    @Override
    public SuccessResponse<UserResponse> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.success(
                SuccessCode.GET_USER_INFO_SUCCESS,
                userService.getUserById(userDetails.getUserId())
        );
    }

    @PatchMapping("/profile")
    @Override
    public SuccessResponse updateUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateUserProfileRequest request
            ) {
        userService.updateUserProfile(userDetails.getUserId(), request.ageGroup(), request.gender(), request.job());
        return SuccessResponse.success(
                SuccessCode.UPDATE_USER_PROFILE_SUCCESS
        );
    }
}
