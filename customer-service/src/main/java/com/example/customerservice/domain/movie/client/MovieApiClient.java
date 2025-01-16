package com.example.customerservice.domain.movie.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class MovieApiClient {

    private final RestTemplate restTemplate;

    private final String KOFIC_URL = "https://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";

    @Value("${kofic.key}")
    private final String API_KEY;

    public MovieApiClient(@Value("${kofic.key}") String API_KEY) {
        this.restTemplate = new RestTemplate();
        this.API_KEY = API_KEY;
    }

    @Scheduled(cron = "*/15 * * * * *", zone = "Asia/Seoul")
    public void getMoviesWeek() {
        LocalDate today = LocalDate.now();
//        String startDate = today.minusDays(7).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String endDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // TODO: 연도 별 가능 이니까 분류를 해야된다.
        String startDate = today.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy"));
        String endDate = today.format(DateTimeFormatter.ofPattern("yyyy"));


        URI uri = UriComponentsBuilder.fromUriString(KOFIC_URL)
                .queryParam("key", API_KEY)
                .queryParam("curPage", "1")
                .queryParam("itemPerPage", "100")
                .queryParam("openStartDt", startDate)
                .queryParam("openEndDt", endDate)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            log.info(responseEntity.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
