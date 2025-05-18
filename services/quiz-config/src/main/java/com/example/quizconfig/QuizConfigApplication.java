package com.example.quizconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QuizConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizConfigApplication.class, args);
	}

}