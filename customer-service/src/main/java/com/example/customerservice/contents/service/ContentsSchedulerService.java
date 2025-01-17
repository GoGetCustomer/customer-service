package com.example.customerservice.contents.service;

import com.example.customerservice.contents.client.BookApiClient;
import com.example.customerservice.contents.entity.Contents;
import com.example.customerservice.contents.repository.ContentsRepository;
import com.example.customerservice.contents.util.AladinUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.example.customerservice.contents.util.AladinUtils.calculateTotalPages;
import static com.example.customerservice.contents.util.AladinUtils.parseTotalResults;

@Service
@RequiredArgsConstructor
public class ContentsSchedulerService {
    private final BookApiClient bookApiClient;
    private final ContentsRepository contentsRepository;


    private Integer checkTotalPages(int maxResults) throws JsonProcessingException {
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

    private void saveContentsToDatabase(List<Contents> contentsList) {
        List<String> existingTitlesAndWriters = contentsRepository.findAllTitlesAndWriters();

        List<Contents> newContents = contentsList.stream()
                .filter(content -> !existingTitlesAndWriters.contains(content.getTitle() + "|" + content.getWriter()))
                .toList();

        contentsRepository.saveAll(newContents);
    }


}
