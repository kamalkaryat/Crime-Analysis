package com.k2dev.ca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CrimeAnalysisApplication {	
	public static void main(String[] args) {
		SpringApplication.run(CrimeAnalysisApplication.class, args);
	}
}
