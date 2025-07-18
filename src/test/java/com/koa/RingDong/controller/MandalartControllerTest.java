package com.koa.RingDong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koa.RingDong.domain.mandalart.dto.UpdateMainBlockRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateSubBlockRequest;
import com.koa.RingDong.domain.mandalart.dto.UpdateCellRequest;
import com.koa.RingDong.domain.mandalart.dto.CellResponse;
import com.koa.RingDong.domain.mandalart.dto.MainBlockResponse;
import com.koa.RingDong.domain.mandalart.dto.SubBlockResponse;
import com.koa.RingDong.domain.mandalart.repository.Status;
import com.koa.RingDong.domain.mandalart.repository.ReminderInterval;
import com.koa.RingDong.security.WithMockCustomUser;
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
    @WithMockCustomUser(userId = 1L)
    void createMandalart() throws Exception {
        mockMvc.perform(post("/api/mandalart"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("만다라트 전체 조회")
    @WithMockCustomUser(userId = 1L)
    void getMandalart() throws Exception {
        // CellResponse 생성
        CellResponse cellResponse = CellResponse.builder()
                .position(0)
                .content("셀 내용")
                .status(Status.UNDONE)
                .build();

        // SubBlockResponse 생성
        SubBlockResponse subBlockResponse = SubBlockResponse.builder()
                .subId(1L)
                .content("서브 내용")
                .status(Status.UNDONE)
                .cells(List.of(cellResponse))
                .build();

        // MainBlockResponse 생성
        MainBlockResponse mainBlockResponse = MainBlockResponse.builder()
                .mainId(1L)
                .content("메인 내용")
                .status(Status.UNDONE)
                .reminderInterval(ReminderInterval.ONE_WEEK)
                .subBlocks(List.of(subBlockResponse))
                .build();

        // Mock Service 동작 정의
        Mockito.when(mandalartService.getMandalart(1L))
                .thenReturn(mainBlockResponse);

        // 실제 호출
        mockMvc.perform(get("/api/mandalart"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("만다라트 전체 수정")
    @WithMockCustomUser(userId = 1L)
    void updateMandalart() throws Exception {
        Long mainId = 1L;

        // 예시 request 생성
        UpdateCellRequest cellRequest = UpdateCellRequest.builder()
                .position(0)
                .content("셀 내용")
                .status(null)
                .build();

        UpdateSubBlockRequest subBlockRequest = UpdateSubBlockRequest.builder()
                .subId(1L)
                .content("서브 내용")
                .status(null)
                .cells(List.of(cellRequest))
                .build();

        UpdateMainBlockRequest request = UpdateMainBlockRequest.builder()
                .content("메인 내용")
                .status(null)
                .reminderInterval(null)
                .subBlockRequests(List.of(subBlockRequest))
                .build();

        mockMvc.perform(patch("/api/mandalart/{mainId}", mainId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
