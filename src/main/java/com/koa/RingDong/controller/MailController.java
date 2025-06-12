package com.koa.RingDong.controller;

import com.koa.RingDong.dto.request.MailRequest;
import com.koa.RingDong.dto.response.ApiResponse;
import com.koa.RingDong.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @Operation(summary = "메일 전송")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendMail(@RequestBody MailRequest request) throws IOException {
        mailService.sendMail(request.getTargetId());
        return ResponseEntity.ok(ApiResponse.success("메일 전송 성공"));
    }
}
