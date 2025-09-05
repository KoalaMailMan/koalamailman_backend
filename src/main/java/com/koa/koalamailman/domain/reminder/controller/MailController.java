package com.koa.koalamailman.domain.reminder.controller;

import com.koa.koalamailman.domain.reminder.service.MailService;
import com.koa.koalamailman.domain.reminder.dto.MailRequest;
import com.koa.koalamailman.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/internal/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendMail(@RequestBody MailRequest request) throws IOException {
        mailService.sendMail(request.getTargetId());
        return ResponseEntity.ok(ApiResponse.success("메일 전송 성공"));
    }
}
