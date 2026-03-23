package com.koa.koalamailman.auth.presentation;

import com.koa.koalamailman.auth.presentation.docs.AuthControllerDocs;
import com.koa.koalamailman.auth.presentation.dto.response.AccessTokenResponse;
import com.koa.koalamailman.auth.application.RefreshTokenService;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
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
            @CookieValue(value = "refresh_token") String refreshToken
    ) {
        return SuccessResponse.success(
                SuccessCode.GET_ACCESS_TOKEN,
                AccessTokenResponse.from(refreshTokenService.validateAndGenerateAccessToken(refreshToken))
        );
    }
}