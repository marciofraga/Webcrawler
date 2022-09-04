package com.overmind.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movie")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titlePt;
    private String titleEn;
    private String path;
    private Integer position;
    private BigDecimal rating;

    @OneToMany(mappedBy = "movie")
    private List<Director> directors;

    @OneToMany(mappedBy = "movie")
    private List<Cast> cast;

    @OneToMany(mappedBy = "movie")
    private List<Review> review;

    @Override
    public boolean equals(Object o) {
        Movie movie = (Movie) o;
        return Objects.equals(path, movie.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titlePt, titleEn, path, rating);
    }
}
