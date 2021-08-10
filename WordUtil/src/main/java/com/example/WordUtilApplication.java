package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class WordUtilApplication {
	
	public static void main(String[] args) {
		//CIdockerHub
		SpringApplication.run(WordUtilApplication.class, args);
		
	}

}
