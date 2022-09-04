package com.overmind.crawler.repository;

import com.overmind.crawler.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByPathIsLike(String path);
}
