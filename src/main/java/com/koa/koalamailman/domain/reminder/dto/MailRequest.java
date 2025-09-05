package com.koa.koalamailman.domain.reminder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
    private Long targetId;
}