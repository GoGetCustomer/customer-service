package com.example.customerservice.contents.controller;

import com.example.customerservice.contents.service.ContentsSchedulerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ScheduledFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class ContentsScheduleController {
    private final ContentsSchedulerService contentsSchedulerService;
    private final ThreadPoolTaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledTask;

    @PostMapping("/new-book")
    public ResponseEntity<String> startBookSync() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            return ResponseEntity.badRequest().body("업데이트 작업이 이미 실행 중입니다.");
        }
        int maxResults = 50;
        /*
        //1분마다 테스트 version

        scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
            try {
                contentsSchedulerService.fetchBooksForAllPages(maxResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, Instant.now(), Duration.ofMinutes(1));
        return ResponseEntity.ok("도서 업데이트 작업이 1분마다 실행되도록 예약되었습니다.");

         */

        scheduledTask = taskScheduler.schedule(() -> {
            try {
                contentsSchedulerService.fetchBooksForAllPages(maxResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger("0 0 0 ? * WED");
            return cronTrigger.nextExecutionTime(triggerContext).toInstant();
        });

        return ResponseEntity.ok("도서 업데이트 작업이 매주 수요일마다 실행되도록 예약되었습니다.");
    }

    @DeleteMapping("/new-book")
    public ResponseEntity<String> stopBookSync() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            return ResponseEntity.ok("도서 업데이트 작업이 중지되었습니다.");
        }
        return ResponseEntity.badRequest().body("현재 실행 중인 업데이트 작업이 없습니다.");
    }
}
