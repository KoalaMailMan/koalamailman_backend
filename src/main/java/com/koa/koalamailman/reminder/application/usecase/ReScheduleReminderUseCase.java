package com.koa.koalamailman.reminder.application.usecase;

import com.koa.koalamailman.mandalart.infrastructure.MandalartRepository;
import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.reminder.domain.ReminderOption;
import com.koa.koalamailman.reminder.application.provider.ReminderTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReScheduleReminderUseCase {

    private final ReminderTimeProvider reminderTimeProvider;
    private final MandalartRepository mandalartRepository;

    @Transactional
    public void rescheduleRandom(Long mandalartId) {
        Mandalart mandalart = mandalartRepository.findById(mandalartId).orElse(null);
        if (mandalart == null) return;

        ReminderOption option = mandalart.getReminderOption();
        if (option == null) return;
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(option.getRemindInterval());
        option.setRemindScheduledAt(nextScheduledTime);
    }
}
