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


}
