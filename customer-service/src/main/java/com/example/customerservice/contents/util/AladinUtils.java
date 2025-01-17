package com.example.customerservice.contents.util;

import com.example.customerservice.contents.entity.Contents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class AladinUtils {
    public static List<Contents> parseContentsData(JsonNode items, String category) {
        List<Contents> contentsList = new ArrayList<>();

        for (JsonNode item : items) {
            String title = item.path("title").asText();
            String writer = item.path("author").asText();
            String summary = item.path("description").asText();
            String image = item.path("cover").asText();

            Contents content = Contents.of(
                    category,
                    title,
                    writer,
                    summary.isEmpty() ? null : summary,
                    image
            );
            contentsList.add(content);
        }

        return contentsList;
    }

    static public Integer parseTotalResults(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        int totalResults = rootNode.path("totalResults").asInt();

        System.out.println("Total Results: " + totalResults);
        return totalResults;
    }

    static public int calculateTotalPages(int totalResults, int maxResults) {
        if (maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be greater than 0");
        }

        return (int) Math.ceil((double) totalResults / maxResults);
    }
}
