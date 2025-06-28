package com.koa.RingDong.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/login")
public class LoginController {

    @Value("${app.oauth2.front-uri}")
    private String frontUri;

    @GetMapping("/naver")
    public String redirectToNaver() {
        return "redirect:" + frontUri + "/oauth2/authorization/naver";
    }

    @GetMapping("/google")
    public String redirectToGoogle() {
        return "redirect:" + frontUri + "/oauth2/authorization/google";
    }
}