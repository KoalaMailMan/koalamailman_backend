package com.koa.koalamailman.reminder.application.usecase;

import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.mandalart.application.MandalartService;
import com.koa.koalamailman.reminder.application.provider.ReminderTimeProvider;
import com.koa.koalamailman.reminder.presentation.dto.request.UpdateReminderOptionsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateReminderOptionUseCase {
    private final MandalartService mandalartService;
    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public void updateReminderOption(Long userId, UpdateReminderOptionsRequest request) {
        Mandalart mandalart = mandalartService.findMandalartByMandalartId(userId, request.mandalartId());
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(request.reminderInterval());

        mandalart.getReminderOption().update(
                request.reminderEnabled(),
                request.reminderInterval(),
                nextScheduledTime
        );
    }
}
