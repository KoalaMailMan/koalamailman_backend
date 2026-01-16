package com.koa.koalamailman.domain.reminder.application.usecase;

import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;
import com.koa.koalamailman.domain.reminder.application.provider.ReminderTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReScheduleReminderUseCase {

    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public void rescheduleRandom(MandalartEntity mandalart) {
        ReminderOption option = mandalart.getReminderOption();
        if (option == null) return;
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(option.getRemindInterval());
        option.setRemindScheduledAt(nextScheduledTime);
    }
}
