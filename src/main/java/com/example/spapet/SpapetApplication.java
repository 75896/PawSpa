package com.example.spapet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpapetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpapetApplication.class, args);
	}

}
