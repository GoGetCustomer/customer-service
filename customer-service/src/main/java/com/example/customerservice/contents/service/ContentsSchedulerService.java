package com.example.customerservice.contents.service;

import com.example.customerservice.contents.client.BookApiClient;
import com.example.customerservice.contents.entity.Contents;
import com.example.customerservice.contents.entity.SchedulerStatus;
import com.example.customerservice.contents.repository.ContentsRepository;
import com.example.customerservice.contents.util.AladinUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static com.example.customerservice.contents.util.AladinUtils.calculateTotalPages;
import static com.example.customerservice.contents.util.AladinUtils.parseTotalResults;


@Service
@RequiredArgsConstructor
public class ContentsSchedulerService {
    private final BookApiClient bookApiClient;
    private final ContentsRepository contentsRepository;
    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;


    public Integer checkTotalPages(int maxResults) throws JsonProcessingException {
        String response = bookApiClient.fetchBooksByPage(1, maxResults);
        int totalResults = parseTotalResults(response);
        return calculateTotalPages(totalResults, maxResults);
    }

    public void fetchBooksForAllPages(int maxResults) throws JsonProcessingException {

        Instant start = Instant.now();

        int totalPages = checkTotalPages(maxResults);
        System.out.println("totalPages = " + totalPages);
        ObjectMapper objectMapper = new ObjectMapper();

        for (int page = 1; page <= totalPages; page++) {
            String pageResponse = bookApiClient.fetchBooksByPage(page, maxResults);
            JsonNode items = objectMapper.readTree(pageResponse).path("item");

            List<Contents> contentsList = AladinUtils.parseContentsData(items, "Book");
            saveContentsToDatabase(contentsList);
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Execution time: " +  timeElapsed.toSeconds() + " seconds");
    }

    void saveContentsToDatabase(List<Contents> contentsList) {
        List<String> existingTitlesAndWriters = contentsRepository.findAllTitlesAndWriters();

        List<Contents> newContents = contentsList.stream()
                .filter(content -> !existingTitlesAndWriters.contains(content.getTitle() + "|" + content.getWriter()))
                .toList();

        contentsRepository.saveAll(newContents);
    }



    public SchedulerStatus startBookSync(int maxResults) {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            return SchedulerStatus.ALREADY_RUNNING;
        }

        scheduledTask = taskScheduler.schedule(() -> {
            try {
                fetchBooksForAllPages(maxResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 처리 중 오류 발생", e);
            }
        }, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger("0 0 0 ? * WED");
            return cronTrigger.nextExecutionTime(triggerContext).toInstant();
        });

        /*
        1분마다 실힝 (테스트용)

        scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
            try {
                fetchBooksForAllPages(maxResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error while processing JSON", e);
            }
        }, Duration.ofMinutes(1));
         */


        return SchedulerStatus.STARTED;
    }

    public SchedulerStatus stopBookSync() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            return SchedulerStatus.STOPPED;
        }
        return SchedulerStatus.NO_TASK_RUNNING;
    }

    public boolean isTaskRunning() {
        return scheduledTask != null && !scheduledTask.isCancelled();
    }
}
