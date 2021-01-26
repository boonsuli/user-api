package com.trailerplan.application;

import com.trailerplan.config.AppDataConfigMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(classes = {AppDataConfigMemory.class})
@SpringBootApplication(scanBasePackages = {"com.trailerplan"})
public class TrailerplanBdMemoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrailerplanBdMemoryApplication.class, args);
	}

}
