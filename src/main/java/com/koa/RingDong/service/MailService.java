package com.koa.RingDong.service;

import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.provider.ReminderTimeProvider;
import com.koa.RingDong.repository.MainBlockRepository;
import com.koa.RingDong.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    final private UserRepository userRepository;
    final private MainBlockRepository mainBlockRepository;
    final private ReminderTimeProvider reminderTimeProvider;

    @Transactional
    public void sendMail(Long targetId) {

        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 ID: " + targetId));

        // contentText를 Mandalart 내용으로
        MainBlock mainBlock = mainBlockRepository.findFullMandalartByUserId(targetId)
                .orElseThrow(() -> new IllegalArgumentException("Main Block이 존재하지 않는 사용자 ID: " + targetId));

        try {
            sendHTMLMail(user.getEmail(), "RingDong", buildHtmlTableFromMainBlock(mainBlock));
        } catch (IOException e) {
            log.error("메일 전송 실패 - userId: {}, email: {}, 이유: {}", targetId, user.getEmail(), e.getMessage());

            // 메일 전송 실패 시 nextScheduledTime 내일로 다시 설정
            mainBlock.setNextScheduledTime(reminderTimeProvider.generateRandomTimeForTomorrow(mainBlock.getReminderInterval()));

            log.info("메일 재전송을 위한 nextScheduledTime 갱신 완료 - {}로 설정됨");
        }
    }

    public void sendHTMLMail(String to, String subject, String htmlContent) throws IOException {
        Email from = new Email("reminder@ringdong.kr");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        SendGrid sg = new SendGrid(apiKey);
        Response response = sg.api(request);

        if (response.getStatusCode() != 202) {
            throw new IOException("SendGrid 응답 실패 - 상태 코드: " + response.getStatusCode());
        }

        log.info("메일 전송 성공 - to: {}, subject: {}", to, subject);
    }

    private String buildHtmlTableFromMainBlock(MainBlock mainBlock) {
        String[] cellContents = new String[9];
        Arrays.fill(cellContents, "");  // 초기화

        // 중앙 목표 (position 4)
        cellContents[4] = "<b>중앙 목표</b><br>" + (mainBlock.getContent() == null ? "미정" : mainBlock.getContent());

        // SubBlock 채우기 (position 0~8 중에서 4는 중앙이므로 제외)
        mainBlock.getSubBlocks().forEach(sub -> {
            int pos = sub.getPosition();
            if (pos >= 0 && pos < 9 && pos != 4) {
                String content = sub.getContent() == null ? "(비어있음)" : sub.getContent();
                cellContents[pos] = content;
            }
        });

        // HTML 테이블 생성
        StringBuilder html = new StringBuilder();
        html.append("<div style=\"width: 100%; max-width: 900px; margin: 0 auto;\">");
        html.append("<table style=\"width: 100%; border-collapse: collapse; text-align: center; background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);\">");

        for (int i = 0; i < 3; i++) {
            html.append("<tr>");
            for (int j = 0; j < 3; j++) {
                int pos = i * 3 + j;
                String cellStyle = "";

                // 중앙 셀 스타일
                if (pos == 4) {
                    cellStyle = "background-color: #fef3c7; font-weight: bold; font-size: 1.1em;";
                } else {
                    // 각 위치별 배경색 설정 (프론트엔드의 pastel 테마와 유사하게)
                    String[] bgColors = {
                            "#eff6ff", // blue-50
                            "#f0fdfa", // teal-50
                            "#ecfeff", // cyan-50
                            "#fffbeb", // amber-50
                            "#fff1f2", // rose-50
                            "#f5f3ff", // violet-50
                            "#ecfdf5", // emerald-50
                            "#fff7ed"  // orange-50
                    };
                    cellStyle = "background-color: " + bgColors[pos > 4 ? pos - 1 : pos] + ";";
                }

                html.append("<td style=\"border: 2px solid #e5e7eb; padding: 16px; " + cellStyle + "\">")
                        .append(cellContents[pos])
                        .append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</div>");
        return html.toString();
    }
}