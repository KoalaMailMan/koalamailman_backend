package com.koa.RingDong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/kako")
    public String redirectToKakao() {
        return "redirect:/oauth2/authorization/kakao";
    }

    @GetMapping("/naver")
    public String redirectToNaver(){
        return "redirect:/oauth2/authorization/naver";
    }
}