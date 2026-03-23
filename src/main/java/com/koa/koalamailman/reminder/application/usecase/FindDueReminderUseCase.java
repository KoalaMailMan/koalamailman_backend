package com.koa.koalamailman.reminder.application.usecase;

import com.koa.koalamailman.mandalart.infrastructure.MandalartRepository;
import com.koa.koalamailman.mandalart.domain.Mandalart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindDueReminderUseCase {

    private final MandalartRepository mandalartRepository;

    @Transactional(readOnly = true)
    public List<Mandalart> findBefore(LocalDateTime until) {
        return mandalartRepository.findDueReminders(until);
    }
}
