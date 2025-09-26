package com.jacek.nutriweek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NutriWeekApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutriWeekApplication.class, args);
	}

}
