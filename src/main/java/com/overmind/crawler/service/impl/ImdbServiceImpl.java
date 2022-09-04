package com.overmind.crawler.service.impl;

import com.overmind.crawler.model.Movie;
import com.overmind.crawler.repository.MovieRepository;
import com.overmind.crawler.service.crawler.DetailCrawler;
import com.overmind.crawler.service.crawler.GeneralCrawler;
import com.overmind.crawler.service.crawler.ReviewCrawler;
import com.overmind.crawler.service.spi.ImdbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImdbServiceImpl implements ImdbService {

    private final MovieRepository movieRepository;
    private final GeneralCrawler generalCrawler;
    private final DetailCrawler detailCrawler;
    private final ReviewCrawler reviewCrawler;

    @Value("${crawler.url.host}")
    private String URL_HOST;

    @Value("${crawler.url}")
    private String URL;

    @Override
    public void updateRanking() {

        try {
            generalCrawler.visit(URL_HOST, "chart/bottom");
            movieRepository.findAll().forEach(movie -> {
                try {
                    detailCrawler.visit(URL.concat(movie.getPath()), movie.getPath());
                    reviewCrawler.visit(URL.concat(movie.getPath()).concat("reviews"), movie.getPath());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Page<Movie> findAll(Pageable page) {
        return movieRepository.findAll(page);
    }

    @Override
    public Movie findMovie(Long id) {

        return movieRepository.findById(id)
                .orElse(null);
    }
}
