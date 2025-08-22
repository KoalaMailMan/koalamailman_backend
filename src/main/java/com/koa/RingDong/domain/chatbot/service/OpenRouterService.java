package com.koa.RingDong.domain.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenRouterService {

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    @Value("${openrouter.api-key}")
    private String API_KEY;

    @Value("${openrouter.model}")
    private String MODEL;
    public String callLLM(String userPrompt) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", userPrompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString();
    }
}