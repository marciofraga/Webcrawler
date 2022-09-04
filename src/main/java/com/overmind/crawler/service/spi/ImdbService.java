package com.overmind.crawler.service.spi;

import com.overmind.crawler.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImdbService {

    void updateRanking();

    Page<Movie> findAll(Pageable page);

    Movie findMovie(Long id);
}
