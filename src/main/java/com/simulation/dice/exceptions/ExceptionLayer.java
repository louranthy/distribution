package com.simulation.dice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExceptionLayer {
	CONTROLLER("CONTROLLER"), SERVICE("SERVICE"), REPOSITORY("REPOSITORY");
	
	@Getter 
	private String value;
}
