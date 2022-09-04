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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cast")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameActor;
    private String nameCharacter;

    @ManyToOne
    @JoinColumn(name="movie_id")
    private Movie movie;
}
