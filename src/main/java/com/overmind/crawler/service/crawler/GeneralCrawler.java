package com.overmind.crawler.service.crawler;

import com.overmind.crawler.model.Movie;
import com.overmind.crawler.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneralCrawler extends AbstractCrawler {

    private final MovieRepository movieRepository;
    private final List<Movie> movies;

    private final static String EXCLUSION_PATH = "?";

    @Override
    public void extractData(Document doc, String path) {
        extractDataFromTable(doc);
        saveOrUpdate(movies);
    }

    private void extractDataFromTable(Document doc) {
        Element table = doc.body().getElementsByAttributeValue("class", "lister-list").first();
        Elements lines = table.getElementsByTag("tr");

        lines.forEach(line -> {
            String path = line.getElementsByAttributeValue("class", "posterColumn").first()
                    .getElementsByTag("a").attr("href");
            String titlePt = line.getElementsByAttributeValue("class", "titleColumn").first()
                    .getElementsByTag("a").html();

            String position = line.getElementsByAttributeValue("class", "titleColumn").first().html();
            position = position.substring(0, position.indexOf("."));

            String rating = line.getElementsByAttributeValue("class", "ratingColumn imdbRating").first()
                            .getElementsByTag("strong").html();
            rating = rating.isEmpty() ? "0": rating;

            movies.add(Movie.builder()
                    .position(Integer.parseInt(position))
                    .path(path.substring(0, path.indexOf(EXCLUSION_PATH)))
                    .titlePt(titlePt)
                    .rating(new BigDecimal(rating)).build());
        });
    }

    private void saveOrUpdate(List<Movie> movies) {
        movies.forEach(movie -> {
            Movie movieSaved = movieRepository.findByPathIsLike(movie.getPath());

            if(Objects.nonNull(movieSaved)) {
                movieSaved.setRating(movie.getRating());
                movieSaved.setTitlePt(movieSaved.getTitlePt());
                movieSaved.setPosition(movie.getPosition());
                movieRepository.save(movieSaved);
            } else {
                movieRepository.save(movie);
            }
        });
    }
}
