package com.koa.RingDong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koa.RingDong.domain.mandalart.dto.UpdateCoreGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateMainGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateSubGoalRequest;
import com.koa.RingDong.domain.mandalart.dto.SubGoalResponse;
import com.koa.RingDong.domain.mandalart.dto.CoreGoalResponse;
import com.koa.RingDong.domain.mandalart.dto.MainGoalResponse;
import com.koa.RingDong.domain.mandalart.repository.Status;
import com.koa.RingDong.domain.mandalart.repository.ReminderInterval;

import com.koa.RingDong.domain.mandalart.service.MandalartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MandalartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MandalartService mandalartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("만다라트 생성")
    void createMandalart() throws Exception {
        mockMvc.perform(post("/api/mandalart"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("만다라트 전체 조회")
    void getMandalart() throws Exception {
        // SubGoalResponse 생성
        SubGoalResponse subGoalResponse = SubGoalResponse.builder()
                .position(0)
                .content("세부 목표 내용")
                .status(Status.UNDONE)
                .build();

        // MainGoalResponse 생성
        MainGoalResponse mainGoalResponse = MainGoalResponse.builder()
                .mainGoalId(1L)
                .content("주요 목표 내용")
                .status(Status.UNDONE)
                .subGoals(List.of(subGoalResponse))
                .build();

        // CoreGoalResponse 생성
        CoreGoalResponse coreGoalResponse = CoreGoalResponse.builder()
                .coreGoalId(1L)
                .content("핵심 목표 내용")
                .status(Status.UNDONE)
                .reminderInterval(ReminderInterval.ONE_WEEK)
                .mainGoals(List.of(mainGoalResponse))
                .build();

        // Mock Service 동작 정의
        Mockito.when(mandalartService.getMandalart(1L))
                .thenReturn(coreGoalResponse);

        // 실제 호출
        mockMvc.perform(get("/api/mandalart"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("만다라트 전체 수정")
    void updateMandalart() throws Exception {
        Long coreGoalId = 1L;

        // 예시 request 생성
        UpdateSubGoalRequest subGoalRequest = UpdateSubGoalRequest.builder()
                .position(0)
                .content("세부 목표 내용")
                .status(null)
                .build();

        UpdateMainGoalRequest mainGoalRequest = UpdateMainGoalRequest.builder()
                .mainGoalId(1L)
                .content("주요 목표 내용")
                .status(null)
                .subGoals(List.of(subGoalRequest))
                .build();

        UpdateCoreGoalRequest request = UpdateCoreGoalRequest.builder()
                .content("핵심 목표 내용")
                .status(null)
                .reminderInterval(null)
                .mainGoalRequests(List.of(mainGoalRequest))
                .build();

        mockMvc.perform(patch("/api/mandalart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
