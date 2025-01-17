package com.example.customerservice.contents.controller;

import com.example.customerservice.contents.entity.SchedulerStatus;
import com.example.customerservice.contents.service.ContentsSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class ContentsScheduleController {
    private final ContentsSchedulerService contentsSchedulerService;

    @PostMapping("/new-book")
    public ResponseEntity<String> startBookSync() {
        SchedulerStatus status = contentsSchedulerService.startBookSync(50);

        switch (status) {
            case ALREADY_RUNNING:
                return ResponseEntity.badRequest().body("업데이트 작업이 이미 실행 중입니다.");
            case STARTED:
                return ResponseEntity.ok("도서 업데이트 작업이 매주 수요일마다 실행되도록 예약되었습니다.");
            default:
                throw new IllegalStateException("Unexpected status: " + status);
        }
    }

    @DeleteMapping("/new-book")
    public ResponseEntity<String> stopBookSync() {
        SchedulerStatus status = contentsSchedulerService.stopBookSync();

        switch (status) {
            case STOPPED:
                return ResponseEntity.ok("도서 업데이트 작업이 중지되었습니다.");
            case NO_TASK_RUNNING:
                return ResponseEntity.badRequest().body("현재 실행 중인 업데이트 작업이 없습니다.");
            default:
                throw new IllegalStateException("Unexpected status: " + status);
        }
    }
}
