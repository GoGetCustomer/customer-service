package com.example.customerservice.contents.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class BookApiClient {
    private final RestTemplate restTemplate;
    private static final String ALADIN_BASE_URL = "https://www.aladin.co.kr/ttb/api/ItemList.aspx";

    @Value("${aladin.key}")
    private final String TTB_KEY;

    public BookApiClient(@Value("${aladin.key}") String TTB_KEY) {
        this.restTemplate = new RestTemplate();
        this.TTB_KEY = TTB_KEY;
    }

    public String fetchBooksByPage(int page, int maxResults) {
        String queryType = "ItemNewAll";
        String coverSize = "Mid";
        String sorting = "PublishTime";
        String output = "js";
        String searchTarget = "Book";

        URI uri = UriComponentsBuilder.fromUriString(ALADIN_BASE_URL)
                .queryParam("ttbkey", TTB_KEY)
                .queryParam("QueryType", queryType)
                .queryParam("Cover", coverSize)
                .queryParam("Sort", sorting)
                .queryParam("start", page)
                .queryParam("SearchTarget", searchTarget)
                .queryParam("MaxResults", maxResults)
                .queryParam("output", output)
                .queryParam("Version", "20131101")
                .build()
                .toUri();

        try {
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch books from Aladin API", e);
        }
    }
}
