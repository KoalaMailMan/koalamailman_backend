package com.koa.koalamailman.domain.reminder.service;

import com.koa.koalamailman.domain.mandalart.repository.MandalartRepository;
import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.ReminderOption;
import com.koa.koalamailman.domain.mandalart.service.MandalartService;
import com.koa.koalamailman.domain.reminder.dto.request.UpdateReminderOptionsRequest;
import com.koa.koalamailman.domain.reminder.provider.ReminderTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final MandalartService mandalartService;
    private final MandalartRepository mandalartRepository;

    private final ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public void rescheduleRandomWithinInterval(ReminderOption reminderOption, Long mandalartId) {
        MandalartEntity mandalart = mandalartService.findMandalartByMandalartId(mandalartId);

        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(reminderOption.getRemindInterval());
        mandalart.getReminderOption().reschedule(nextScheduledTime);
    }

    @Transactional
    public void rescheduleTomorrow(ReminderOption reminderOption, Long mandalartId) {
        MandalartEntity mandalart = mandalartService.findMandalartByMandalartId(mandalartId);

        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTimeForTomorrow();
        mandalart.getReminderOption().reschedule(nextScheduledTime);
    }

    @Transactional(readOnly = true)
    public List<MandalartEntity> findMandalartsByScheduleTimeBefore(LocalDateTime endOfToday) {
        return mandalartRepository.findDueReminders(endOfToday);
    }

    @Transactional
    public void updateReminderOption(UpdateReminderOptionsRequest request) {
        MandalartEntity mandalart = mandalartService.findMandalartByMandalartId(request.mandalartId());
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(request.reminderInterval());

        mandalart.getReminderOption().update(
                request.reminderEnabled(),
                request.reminderInterval(),
                nextScheduledTime
                );
    }
}
