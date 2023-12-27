package com.rhymthwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MusicStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicStreamingApplication.class, args);
	}

}
