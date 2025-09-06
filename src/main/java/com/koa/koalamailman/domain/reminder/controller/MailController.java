package com.koa.koalamailman.domain.reminder.controller;

import com.koa.koalamailman.domain.reminder.service.MailService;
import com.koa.koalamailman.domain.reminder.dto.MailRequest;
import com.koa.koalamailman.global.dto.SuccessResponse;
import com.koa.koalamailman.global.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
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
    public SuccessResponse<Void> sendMail(@RequestBody MailRequest request) throws IOException {
        mailService.sendMail(request.targetId());
        return SuccessResponse.success(
                SuccessCode.SEND_MAIL_SUCCESS
        );
    }
}
