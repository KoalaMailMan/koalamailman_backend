package com.koa.koalamailman.reminder.application.mail;

import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.Mandalart;
import com.koa.koalamailman.mandalart.domain.GoalService;
import com.koa.koalamailman.reminder.domain.MailMandalartGrid;
import com.koa.koalamailman.reminder.domain.MandalartReminder;
import com.koa.koalamailman.user.domain.User;
import com.koa.koalamailman.user.application.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemindMailService {
    private final MailSender mailSender;
    private final UserUseCase userUseCase;
    private final GoalService goalService;
    private final MandalartMailComposer composer;

    @Value("${mail.tip}")
    private String tip;

    public void send(Mandalart mandalart) {
        User user = userUseCase.findUserById(mandalart.getUserId());
        List<Goal> goals = goalService.getCoreAndMainGoalsFromMandalart(mandalart);

        MandalartReminder reminder = new MandalartReminder(
                user.getNickname(),
                user.getEmail(),
                MailMandalartGrid.from(goals),
                tip
        );

        mailSender.send(composer.compose(reminder));
    }
}
