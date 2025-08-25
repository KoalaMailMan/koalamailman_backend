package com.koa.RingDong.domain.user.controller;

import com.koa.RingDong.domain.user.controller.docs.UserControllerDocs;
import com.koa.RingDong.domain.user.dto.UpdateUserProfileRequest;
import com.koa.RingDong.domain.user.service.UserService;
import com.koa.RingDong.domain.user.dto.UserResponse;
import com.koa.RingDong.global.dto.SuccessResponse;
import com.koa.RingDong.global.exception.SuccessCode;
import com.koa.RingDong.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
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

    @Operation(summary = "유저 프로필(연령대, 성별, 직업) 수정")
    @PatchMapping("/profile")
    public void updateUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateUserProfileRequest request
            ) {
        userService.updateUserProfile(userDetails.getUserId(), request.ageGroup(), request.gender(), request.job());
    }
}
