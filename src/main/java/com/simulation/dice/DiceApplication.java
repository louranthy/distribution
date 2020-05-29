package com.simulation.dice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
@EnableMongoAuditing
@SpringBootApplication
public class DiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiceApplication.class, args);
	}

}
