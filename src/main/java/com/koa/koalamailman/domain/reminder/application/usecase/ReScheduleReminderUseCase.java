package com.koa.koalamailman.domain.reminder.application.usecase;

import com.koa.koalamailman.domain.mandalart.repository.MandalartRepository;
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
    private final MandalartRepository mandalartRepository;

    @Transactional
    public void rescheduleRandom(Long mandalartId) {
        MandalartEntity mandalart = mandalartRepository.findById(mandalartId).orElse(null);
        if (mandalart == null) return;

        ReminderOption option = mandalart.getReminderOption();
        if (option == null) return;
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(option.getRemindInterval());
        option.setRemindScheduledAt(nextScheduledTime);
    }
}
