package com.example.customerservice.contents.util;

import com.example.customerservice.contents.entity.Contents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AladinUtils {



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
