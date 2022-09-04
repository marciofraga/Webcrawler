package com.overmind.crawler.service.crawler;

import com.overmind.crawler.model.Movie;
import com.overmind.crawler.model.Review;
import com.overmind.crawler.repository.MovieRepository;
import com.overmind.crawler.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReviewCrawler extends AbstractCrawler {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void extractData(Document doc, String path) {
        Elements reviews = doc.body().getElementsByAttributeValue("class", "lister-item-content");
        Movie movie = movieRepository.findByPathIsLike(path);
        saveOrUpdateReview(movie, reviews);
    }

    private void saveOrUpdateReview(Movie movie, Elements reviews) {
        List<Review> reviewList = reviewRepository.findAllByMovie(movie);

        reviews.forEach(review -> {
            Element ratings = Optional.ofNullable(review.getElementsByAttributeValue("class", "rating-other-user-rating").first())
                    .orElse(null);

            int valueRating = 0;
            if(Objects.nonNull(ratings)) {
                String rating = ratings.getElementsByTag("span").eq(1).first().html();
                valueRating = Integer.parseInt(rating);
            }

            if(valueRating >= 5) {
                String title = review.getElementsByAttributeValue("class", "title").html();
                String titleExist = reviewList.stream()
                            .map(Review::getTitle)
                            .filter(rev -> rev.equals(title))
                            .findFirst().orElse(null);

                if(Objects.isNull(titleExist)) {
                    String name = review.getElementsByAttributeValue("class", "display-name-date").first()
                            .getElementsByTag("a").first().html();
                    String content = review.getElementsByAttributeValue("class", "content").first()
                            .getElementsByTag("div").eq(1).html();

                    reviewList.add(Review.builder()
                            .movie(movie)
                            .title(title)
                            .name(name)
                            .content(content)
                            .rating(new BigDecimal(valueRating)).build());
                }
            }
        });
        reviewRepository.saveAll(reviewList);
    }

}
