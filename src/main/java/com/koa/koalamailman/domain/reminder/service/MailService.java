package com.koa.koalamailman.domain.reminder.service;

import com.koa.koalamailman.domain.reminder.client.SesMailClient;
import com.koa.koalamailman.domain.reminder.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final SesMailClient mailClient;

    public void send(EmailMessage message) {
        mailClient.send(message);
    }

}
