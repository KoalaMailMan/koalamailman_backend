package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.UpdateUserRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.dto.response.UserResponse;
import com.koa.RingDong.security.CustomUserDetails;
import com.koa.RingDong.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 정보 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponse response = userService.getUserInfo(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("유저 정보 조회 성공", response));
    }

    @Operation(summary = "유저 닉네임 수정")
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        UserResponse response = userService.updateUserNickname(userDetails.getUserId(), request.getNickname());
        return ResponseEntity.ok(ApiResponse.success("유저 닉네임 수정 성공", response));
    }

}
