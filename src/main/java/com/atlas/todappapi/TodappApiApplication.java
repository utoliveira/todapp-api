package com.atlas.todappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.atlas.todappapi.bean")
public class TodappApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodappApiApplication.class, args);
	}

}
