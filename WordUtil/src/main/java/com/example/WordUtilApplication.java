package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class WordUtilApplication {
	
	public static void main(String[] args) {
		//GKE action
		SpringApplication.run(WordUtilApplication.class, args);
		
	}

}
