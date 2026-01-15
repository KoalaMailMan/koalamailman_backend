package com.koa.koalamailman.domain.reminder.presentation;

import com.koa.koalamailman.domain.reminder.domain.EmailMessage;
import com.koa.koalamailman.domain.reminder.application.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/mail")
@RequiredArgsConstructor
public class AdminMailController {

    private final MailService mailService;

    @Operation(summary = "mail send ADMIN 테스트용 api입니다.")
    @PostMapping("/send")
    public String send(EmailMessage message) {
        mailService.send(message);
        return "OK";
    }
}
