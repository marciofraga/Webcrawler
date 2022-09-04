package com.overmind.crawler.repository;

import com.overmind.crawler.model.Movie;
import com.overmind.crawler.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMovie(Movie movie);
}
