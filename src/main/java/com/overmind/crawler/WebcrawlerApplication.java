package com.overmind.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.overmind.crawler.model"})
@EnableJpaRepositories({"com.overmind.crawler.repository"})
public class WebcrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebcrawlerApplication.class, args);
	}

}
