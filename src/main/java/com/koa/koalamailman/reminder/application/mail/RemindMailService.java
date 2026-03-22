package com.koa.koalamailman.reminder.application.mail;

import com.koa.koalamailman.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.mandalart.service.GoalService;
import com.koa.koalamailman.reminder.domain.MandalartGrid;
import com.koa.koalamailman.reminder.domain.MandalartReminder;
import com.koa.koalamailman.user.repository.User;
import com.koa.koalamailman.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemindMailService {
    private final MailSender mailSender;
    private final UserService userService;
    private final GoalService goalService;
    private final MandalartMailComposer composer;

    @Value("${mail.tip}")
    private String tip;

    public void send(MandalartEntity mandalart) {
        User user = userService.findUserById(mandalart.getUserId());
        List<GoalEntity> goals = goalService.getCoreAndMainGoalsFromMandalart(mandalart);

        MandalartReminder reminder = new MandalartReminder(
                user.getNickname(),
                user.getEmail(),
                MandalartGrid.from(goals),
                tip
        );

        mailSender.send(composer.compose(reminder));
    }
}
