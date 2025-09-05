package com.koa.koalamailman.global.config;

import com.koa.koalamailman.global.security.handler.OAuth2FailureHandler;
import com.koa.koalamailman.global.security.handler.OAuth2SuccessHandler;
import com.koa.koalamailman.global.security.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Value("${app.oauth2.front-uri}")
    private String frontUri;

    private static final String OAUTH2_AUTHORIZATION_BASE_URI = "/auth/login/oauth2";
    private static final String LOGOUT_URL = "/";

    @Bean
    @Order(1)
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher(
                OAUTH2_AUTHORIZATION_BASE_URI + "/**",
                "/oauth2/**",
                "/login/**"
        );

        http
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri(OAUTH2_AUTHORIZATION_BASE_URI)
                        )
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, auth) -> {
                            response.sendRedirect(LOGOUT_URL);
                        })
                        .deleteCookies("JSESSIONID", "access_token")
                );
        return http.build();
    }
}