package com.koa.koalamailman.domain.reminder.client;

import com.koa.koalamailman.domain.reminder.dto.EmailMessage;
import com.koa.koalamailman.domain.reminder.dto.MandalartEmailMessage;
import com.koa.koalamailman.domain.reminder.util.MailContentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
@Slf4j
public class SesMailClient implements MailClient {

    private final SesClient sesClient;
    private final String charset;


    public SesMailClient(
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secret,
            @Value("${aws.region:ap-northeast-2}") String region,
            @Value("${mail.charset:UTF-8}") String charset
    ) {
        this.sesClient = SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secret))
                )
                .build();
        this.charset = charset;
    }

    @Override
    public String getProvider() {
        return "ses";
    }

    @Override
    public void send(EmailMessage msg) {

        Body body;
        if (msg.getHtml() != null) {
            body = Body.builder()
                    .html(Content.builder().data(msg.getHtml()).charset(charset).build())
                    .text(msg.getText() != null ? Content.builder().data(msg.getText()).charset(charset).build() : null)
                    .build();
        } else {
            body = Body.builder()
                    .text(Content.builder().data(msg.getText()).charset(charset).build())
                    .build();
        }

        Message message = Message.builder()
                .subject(Content.builder().data(msg.getSubject()).charset(charset).build())
                .body(body)
                .build();

        SendEmailRequest req = SendEmailRequest.builder()
                .source(msg.getFrom())
                .destination(Destination.builder().toAddresses(msg.getTo()).build())
                .message(message)
                .build();

        sesClient.sendEmail(req);
    }

    public void sendMandalart(MandalartEmailMessage msg) {
        try {
            String html = MailContentBuilder.build(msg);

            Body body = Body.builder()
                    .html(Content.builder().data(html).charset(charset).build())
                    .build();

            Message message = Message.builder()
                    .subject(Content.builder().data(msg.subject()).charset(charset).build())
                    .body(body)
                    .build();

            SendEmailRequest req = SendEmailRequest.builder()
                    .source(msg.from())
                    .destination(Destination.builder().toAddresses(msg.to()).build())
                    .message(message)
                    .build();

            sesClient.sendEmail(req);

        } catch (AwsServiceException e) {
            // AWS 서비스 문제
            log.error("[메일] AWS 서비스 에러: {}", e.awsErrorDetails(), e);
        } catch (SdkClientException e) {
            // 네트워크/클라이언트 문제
            log.error("[메일] SDK 클라이언트 에러: {}", e.getMessage(), e);
        } catch (Exception e) {
            // 기타 문제
            log.error("[메일] 알 수 없는 메일 전송 에러: {}", e.getMessage(), e);
        }
    }
}
