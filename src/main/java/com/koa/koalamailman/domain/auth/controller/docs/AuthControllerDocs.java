package com.koa.koalamailman.domain.auth.controller.docs;

import com.koa.koalamailman.domain.auth.dto.AccessTokenResponse;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;

@SecurityRequirement(name = "Authorization")
@Tag(name = "로그인, 인증", description = "로그인 인증 관련 API입니다.")
public interface AuthControllerDocs {
    @Operation(
            summary = "구글 로그인 [GET] /api/auth/login/google",
            description = """
                    ###  성공 리다이렉트 예시
                        https://front.example.com/?access_token={JWT}
                        
                    프론트엔드는 이 토큰을 저장하여 후 이후 API 요청 시 `Authorization: Bearer {token}` 헤더에 실어주면 됩니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 후 JWT 발급"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    default void googleLogin() {}

    @Operation(
            summary = "네이버 로그인 [GET] /api/auth/login/naver",
            description = """
                    ###  성공 리다이렉트 예시
                        https://front.example.com/?access_token={JWT}
                        
                    프론트엔드는 이 토큰을 저장하여 후 이후 API 요청 시 `Authorization: Bearer {token}` 헤더에 실어주면 됩니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 후 JWT 발급"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    default void naverLogin() {}

    @Operation(
            summary = "로그아웃 [POST] /api/auth/logout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "401", description = "이미 로그아웃되었거나 인증되지 않음")
            }
    )
    default void logout() {}

    @Operation(
            summary = "리프레시 토큰으로 액세스 토큰 재발급",
            description = """
            HttpOnly 쿠키의 `refresh_token` 값을 이용해 Access Token을 재발급합니다.
            - 요청: Cookie에 refresh_token 포함
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "재발급 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패(쿠키 없음/만료/불일치)")
            }
    )
    public SuccessResponse<AccessTokenResponse> refreshAccessToken(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @CookieValue(value = "refresh_token") String refreshToken
    );
}
