package com.koa.koalamailman.domain.auth.controller;

import com.koa.koalamailman.domain.auth.controller.docs.AuthControllerDocs;
import com.koa.koalamailman.domain.auth.dto.AccessTokenResponse;
import com.koa.koalamailman.domain.auth.service.RefreshTokenService;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public SuccessResponse<AccessTokenResponse> refreshAccessToken(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @CookieValue(value = "refresh_token") String refreshToken
    ) {
        return SuccessResponse.success(
                SuccessCode.GET_ACCESS_TOKEN,
                AccessTokenResponse.from(refreshTokenService.validateAndGenerateAccessToken(userDetails.getUser(), refreshToken))
        );
    }
}