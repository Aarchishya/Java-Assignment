package com.example.reportgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReportGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReportGeneratorApplication.class, args);
	}
}