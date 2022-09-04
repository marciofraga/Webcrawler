package com.overmind.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "review")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal rating;
    private String title;
    private String name;

    @Column(length = 10000)
    private String content;

    @ManyToOne
    @JoinColumn(name="movie_id")
    private Movie movie;
}
