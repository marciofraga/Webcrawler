package com.overmind.crawler.service.crawler;

import com.overmind.crawler.model.Cast;
import com.overmind.crawler.model.Director;
import com.overmind.crawler.model.Movie;
import com.overmind.crawler.repository.CastRepository;
import com.overmind.crawler.repository.DirectorRepository;
import com.overmind.crawler.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DetailCrawler extends AbstractCrawler {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final CastRepository castRepository;

    private final static String EXCLUSION_TITLE_EN = ": ";

    @Override
    public void extractData(Document doc, String path) {
        Movie movie = movieRepository.findByPathIsLike(path);

        Element titleEn = doc.body().getElementsByAttributeValue("data-testid", "hero-title-block__original-title").first();
        Elements directors = doc.body().getElementsByAttributeValue("data-testid", "title-pc-principal-credit").first()
                .getElementsByTag("a");
        Elements cast = doc.body().getElementsByAttributeValue("data-testid", "title-cast-item");

        saveOrUpdateMovie(movie, titleEn);
        saveOrUpdateDirector(movie, directors);
        saveOrUpdateCast(movie, cast);
    }

    private void saveOrUpdateDirector(Movie movie, Elements directors) {
        List<Director> directorList = directorRepository.findAllByMovie(movie);
        directors.forEach(director -> {
            String nameDirector = directorList.stream()
                    .map(Director::getName)
                    .filter(name -> name.equals(director.html()))
                    .findFirst().orElse(null);

            if(Objects.isNull(nameDirector)) {
                directorList.add(Director.builder()
                        .name(director.html())
                        .movie(movie).build());
            }
        });
        directorRepository.saveAll(directorList);
    }

    private void saveOrUpdateMovie(Movie movie, Element title) {
        String titleEn = movie.getTitlePt();
        if(Objects.nonNull(title)) {
            titleEn = title.html().substring(title.html().indexOf(EXCLUSION_TITLE_EN) + 1).trim();
        }

        if(!titleEn.equals(movie.getTitleEn())) {
            movie.setTitleEn(titleEn);
            movieRepository.save(movie);
        }
    }

    private void saveOrUpdateCast(Movie movie, Elements casts) {
        List<Cast> castList = castRepository.findAllByMovie(movie);
        casts.forEach(cast -> {
            String actor = cast.getElementsByAttributeValue("data-testid", "title-cast-item__actor").first().html();
            String nameActor = castList.stream()
                    .map(Cast::getNameActor)
                    .filter(name -> name.equals(actor))
                    .findFirst().orElse(null);

            String character = Optional.ofNullable(cast.getElementsByAttributeValue("data-testid", "cast-item-characters-link").first())
                    .orElse(new Element("span"))
                    .getElementsByTag("span").first().html();

            String nameCharacter = castList.stream()
                    .map(Cast::getNameCharacter)
                    .filter(name -> name.equals(character))
                    .findFirst().orElse(null);

            if(Objects.isNull(nameActor) && Objects.isNull(nameCharacter)) {
                castList.add(Cast.builder()
                        .nameActor(actor)
                        .nameCharacter(character)
                        .movie(movie).build());
            }
        });
        castRepository.saveAll(castList);
    }
}
