package com.example.customerservice.domain.movie.controller;

import com.example.customerservice.domain.movie.client.MovieApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieApiClient movieApiClient;

    @GetMapping("/week")
    public String getMovie() {
        movieApiClient.getMoviesWeek();
        return "success";
    }
}
