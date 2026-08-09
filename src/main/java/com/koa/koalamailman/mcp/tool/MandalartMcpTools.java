package com.koa.koalamailman.mcp.tool;

import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.AuthErrorCode;
import com.koa.koalamailman.global.security.oauth.CustomUserDetails;
import com.koa.koalamailman.mandalart.application.MandalartUseCase;
import com.koa.koalamailman.mandalart.presentation.dto.response.MandalartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MandalartMcpTools {

    private final MandalartUseCase mandalartUseCase;

    @Tool(
            name = "get_mandalart",
            description = "Get the authenticated user's mandalart goals, including core, main, and sub goals."
    )
    public MandalartResponse getMandalart() {
        Long userId = getAuthenticatedUserId();
        return MandalartResponse.from(mandalartUseCase.getMandalartWithRemind(userId));
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }

        return userDetails.getUserId();
    }
}
