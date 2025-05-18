package com.example.quizconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class QuizConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizConfigApplication.class, args);
	}

}