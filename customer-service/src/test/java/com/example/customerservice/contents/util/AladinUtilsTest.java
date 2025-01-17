package com.example.customerservice.contents.util;

import com.example.customerservice.contents.entity.Contents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AladinUtilsTest {

    @Test
    @DisplayName("JSON 데이터를 Contents 리스트로 파싱합니다.")
    void parseContentsData_ShouldReturnContentsList() throws JsonProcessingException {
        // given
        String mockJson = """
           {
           "item": [
             {
               "title": "밤의 몽상가들",
               "link": "http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=355962381&amp;partner=openAPI&amp;start=api",
               "author": "뤼도빅 에스캉드 (지은이), 김남주 (옮긴이)",
               "pubDate": "2025-01-23",
               "description": "21세기를 사는 현대인의 영혼을 잠식하는 불안하고 각박한 현실에 끊임없이 시달리면서도 삶의 근간인 가족과 친구, 문학에 대한 사랑을 통해 자유의지를 지켜내는 소박하면서도 위대한 이들의 일상을 그린 소설. 뤼도빅 에스캉드의 자전적인 이야기가 담겨 있는 이 소설에는 우리에게 생소한 도시 등반, 즉 ‘높은 곳을 향하여’ 남몰래 건물을 올라가 대도시 파리의 지붕 위를 탐사하는 두 남자의 모습이 그려진다.",
               "isbn": "K602036998",
               "isbn13": "9791159924309",
               "itemId": 355962381,
               "priceSales": 15120,
               "priceStandard": 16800,
               "mallType": "BOOK",
               "stockStatus": "",
               "mileage": 840,
               "cover": "https://image.aladin.co.kr/product/35596/23/coversum/k602036998_1.jpg",
               "categoryId": 50921,
               "categoryName": "국내도서\\u003E소설/시/희곡\\u003E프랑스소설",
               "publisher": "알마",
               "salesPoint": 0,
               "adult": false,
               "fixedPrice": true,
               "customerReviewRank": 0,
               "seriesInfo": {
                 "seriesId": 114027,
                 "seriesLink": "http://www.aladin.co.kr/shop/common/wseriesitem.aspx?SRID=114027&amp;partner=openAPI",
                 "seriesName": "알마 인코그니타 "
               },
               "subInfo": {
    
               }
             },
             {
               "title": "비베카난다의 명상 - 요가와 베단타의 명상",
               "link": "http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=355962553&amp;partner=openAPI&amp;start=api",
               "author": "스와미 비베카난다 (지은이), 김재민 (옮긴이)",
               "pubDate": "2025-01-23",
               "description": "",
               "isbn": "K612036998",
               "isbn13": "9791185062501",
               "itemId": 355962553,
               "priceSales": 15300,
               "priceStandard": 17000,
               "mallType": "BOOK",
               "stockStatus": "",
               "mileage": 850,
               "cover": "https://image.aladin.co.kr/product/35596/25/coversum/k612036998_1.jpg",
               "categoryId": 51569,
               "categoryName": "국내도서\\u003E종교/역학\\u003E명상/선",
               "publisher": "지혜의나무",
               "salesPoint": 0,
               "adult": false,
               "fixedPrice": true,
               "customerReviewRank": 0,
               "subInfo": {
    
               }
             }  
           ]
           }
        """;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode items = objectMapper.readTree(mockJson).path("item");

        // when
        List<Contents> contentsList = AladinUtils.parseContentsData(items, "Book");

        // then
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(contentsList)
            .hasSize(2)
            .satisfies(contents -> {
                Contents firstContent = contents.get(0);
                softly.assertThat(firstContent.getCategory()).as("First content category").isEqualTo("Book");
                softly.assertThat(firstContent.getTitle()).as("First content title").isEqualTo("밤의 몽상가들");
                softly.assertThat(firstContent.getWriter()).as("First content writer").isEqualTo("뤼도빅 에스캉드 (지은이), 김남주 (옮긴이)");
                softly.assertThat(firstContent.getSummary()).as("First content summary").isEqualTo("21세기를 사는 현대인의 영혼을 잠식하는 불안하고 각박한 현실에 끊임없이 시달리면서도 삶의 근간인 가족과 친구, 문학에 대한 사랑을 통해 자유의지를 지켜내는 소박하면서도 위대한 이들의 일상을 그린 소설. 뤼도빅 에스캉드의 자전적인 이야기가 담겨 있는 이 소설에는 우리에게 생소한 도시 등반, 즉 ‘높은 곳을 향하여’ 남몰래 건물을 올라가 대도시 파리의 지붕 위를 탐사하는 두 남자의 모습이 그려진다.");
                softly.assertThat(firstContent.getImage()).as("First content image").isEqualTo("https://image.aladin.co.kr/product/35596/23/coversum/k602036998_1.jpg");

                Contents secondContent = contents.get(1);
                softly.assertThat(secondContent.getCategory()).as("Second content category").isEqualTo("Book");
                softly.assertThat(secondContent.getTitle()).as("Second content title").isEqualTo("비베카난다의 명상 - 요가와 베단타의 명상");
                softly.assertThat(secondContent.getWriter()).as("Second content writer").isEqualTo("스와미 비베카난다 (지은이), 김재민 (옮긴이)");
                softly.assertThat(secondContent.getSummary()).as("Second content summary").isNull();
                softly.assertThat(secondContent.getImage()).as("Second content image").isEqualTo("https://image.aladin.co.kr/product/35596/25/coversum/k612036998_1.jpg");
            });

        softly.assertAll();
    }

    @Test
    @DisplayName("totalResults를 파싱하여 반환합니다.")
    void parseTotalResults_ShouldReturnTotalResults() throws JsonProcessingException {
        // given
        String mockResponse = """
            {
                       "version": "20131101",
                       "logo": "http://image.aladin.co.kr/img/header/2011/aladin_logo_new.gif",
                       "title": "알라딘 전체 신간 리스트 - 국내도서",
                       "link": "https://www.aladin.co.kr/shop/common/wnew.aspx?NewType=New&amp;BranchType=1&amp;partner=openAPI",
                       "pubDate": "Thu, 16 Jan 2025 06:06:04 GMT",
                       "totalResults": 1000,
                       "startIndex": 1,
                       "itemsPerPage": 10,
                       "query": "QueryType=ITEMNEWALL;SearchTarget=Book",
                       "searchCategoryId": 0,
                       "searchCategoryName": "국내도서",
                       "item": [
                         {
                           "title": "밤의 몽상가들",
                           "link": "http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=355962381&amp;partner=openAPI&amp;start=api",
                           "author": "뤼도빅 에스캉드 (지은이), 김남주 (옮긴이)",
                           "pubDate": "2025-01-23",
                           "description": "21세기를 사는 현대인의 영혼을 잠식하는 불안하고 각박한 현실에 끊임없이 시달리면서도 삶의 근간인 가족과 친구, 문학에 대한 사랑을 통해 자유의지를 지켜내는 소박하면서도 위대한 이들의 일상을 그린 소설. 뤼도빅 에스캉드의 자전적인 이야기가 담겨 있는 이 소설에는 우리에게 생소한 도시 등반, 즉 ‘높은 곳을 향하여’ 남몰래 건물을 올라가 대도시 파리의 지붕 위를 탐사하는 두 남자의 모습이 그려진다.",
                           "isbn": "K602036998",
                           "isbn13": "9791159924309",
                           "itemId": 355962381,
                           "priceSales": 15120,
                           "priceStandard": 16800,
                           "mallType": "BOOK",
                           "stockStatus": "",
                           "mileage": 840,
                           "cover": "https://image.aladin.co.kr/product/35596/23/coversum/k602036998_1.jpg",
                           "categoryId": 50921,
                           "categoryName": "국내도서\\u003E소설/시/희곡\\u003E프랑스소설",
                           "publisher": "알마",
                           "salesPoint": 0,
                           "adult": false,
                           "fixedPrice": true,
                           "customerReviewRank": 0,
                           "seriesInfo": {
                             "seriesId": 114027,
                             "seriesLink": "http://www.aladin.co.kr/shop/common/wseriesitem.aspx?SRID=114027&amp;partner=openAPI",
                             "seriesName": "알마 인코그니타 "
                           },
                           "subInfo": {
                
                           }
                         }
                     ]
                    }  
        """;

        // when
        int totalResults = AladinUtils.parseTotalResults(mockResponse);

        // then
        assertThat(totalResults).isEqualTo(1000);
    }

    @Test
    @DisplayName("총 결과와 페이지 크기를 기준으로 전체 페이지 수를 계산합니다.")
    void calculateTotalPages_ShouldReturnCorrectTotalPages() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(AladinUtils.calculateTotalPages(101, 50)).isEqualTo(3); // 101 results, 50 per page = 3 pages
        softly.assertThat(AladinUtils.calculateTotalPages(100, 50)).isEqualTo(2); // 100 results, 50 per page = 2 pages
        softly.assertThat(AladinUtils.calculateTotalPages(1, 50)).isEqualTo(1);   // 1 result, 50 per page = 1 page
        softly.assertAll();
    }

    @Test
    @DisplayName("maxResults 값이 0이거나 음수일 때 IllegalArgumentException 예외를 발생시킵니다.")
    void calculateTotalPages_ShouldThrowExceptionForInvalidMaxResults() {
        // Given & When
        IllegalArgumentException exception =
                org.assertj.core.api.Assertions.catchThrowableOfType(() ->
                        AladinUtils.calculateTotalPages(100, 0), IllegalArgumentException.class);

        // Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(exception).isNotNull();
        softly.assertThat(exception.getMessage()).isEqualTo("maxResults must be greater than 0");
        softly.assertAll();
    }
}
