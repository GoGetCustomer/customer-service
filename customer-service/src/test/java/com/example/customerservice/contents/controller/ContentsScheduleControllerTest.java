package com.example.customerservice.contents.controller;

import com.example.customerservice.contents.entity.SchedulerStatus;
import com.example.customerservice.contents.service.ContentsSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ContentsScheduleControllerTest {

    @Mock
    private ContentsSchedulerService contentsSchedulerService;

    @InjectMocks
    private ContentsScheduleController contentsScheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("새로운 책 업데이트 작업 시작 - 성공")
    void startBookSync_ShouldReturnSuccessResponse() {
        // Given
        when(contentsSchedulerService.startBookSync(50)).thenReturn(SchedulerStatus.STARTED);

        // When
        ResponseEntity<String> response = contentsScheduleController.startBookSync();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("도서 업데이트 작업이 매주 수요일마다 실행되도록 예약되었습니다.");
        verify(contentsSchedulerService, times(1)).startBookSync(50);
    }

    @Test
    @DisplayName("새로운 책 업데이트 작업 시작 - 이미 실행 중")
    void startBookSync_ShouldReturnErrorWhenAlreadyRunning() {
        // Given
        when(contentsSchedulerService.startBookSync(50)).thenReturn(SchedulerStatus.ALREADY_RUNNING);

        // When
        ResponseEntity<String> response = contentsScheduleController.startBookSync();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("업데이트 작업이 이미 실행 중입니다.");
        verify(contentsSchedulerService, times(1)).startBookSync(50);
    }

    @Test
    @DisplayName("책 업데이트 작업 중지 - 성공")
    void stopBookSync_ShouldReturnSuccessResponse() {
        // Given
        when(contentsSchedulerService.stopBookSync()).thenReturn(SchedulerStatus.STOPPED);

        // When
        ResponseEntity<String> response = contentsScheduleController.stopBookSync();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("도서 업데이트 작업이 중지되었습니다.");
        verify(contentsSchedulerService, times(1)).stopBookSync();
    }

    @Test
    @DisplayName("책 업데이트 작업 중지 - 실행 중인 작업 없음")
    void stopBookSync_ShouldReturnErrorWhenNoTaskRunning() {
        // Given
        when(contentsSchedulerService.stopBookSync()).thenReturn(SchedulerStatus.NO_TASK_RUNNING);

        // When
        ResponseEntity<String> response = contentsScheduleController.stopBookSync();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("현재 실행 중인 업데이트 작업이 없습니다.");
        verify(contentsSchedulerService, times(1)).stopBookSync();
    }
}
