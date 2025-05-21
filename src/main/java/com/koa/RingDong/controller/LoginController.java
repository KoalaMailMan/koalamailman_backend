package com.koa.RingDong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String redirectToKakao() {
        return "redirect:/oauth2/authorization/kakao";
    }
}