package com.overmind.crawler.repository;

import com.overmind.crawler.model.Director;
import com.overmind.crawler.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

    List<Director> findAllByMovie(Movie movie);
}
