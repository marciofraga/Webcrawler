package com.overmind.crawler.repository;

import com.overmind.crawler.model.Cast;
import com.overmind.crawler.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastRepository extends JpaRepository<Cast, Long> {

    List<Cast> findAllByMovie(Movie movie);
}
