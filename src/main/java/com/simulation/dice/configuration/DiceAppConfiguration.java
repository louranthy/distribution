package com.simulation.dice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class DiceAppConfiguration {
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}
}
