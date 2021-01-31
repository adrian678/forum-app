package com.github.adrian678.forum.forumapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication

public class ForumAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumAppApplication.class, args);
	}

}
