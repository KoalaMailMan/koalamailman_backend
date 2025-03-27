package com.koa.RingDong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**").permitAll() // 메인 페이지, 로그인은 모두 허용
                        .anyRequest().authenticated() // 그 외는 인증 필요
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/loginSuccess") // 로그인 성공 시 이동할 URL
                );

        return http.build();

    }

}
