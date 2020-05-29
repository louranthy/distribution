package com.simulation.dice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SimulationStatus {
	SUCCESS("SUCCESS"), ERROR("FAILED"), INVALID_INPUTS("INVALID");
	
	@Getter 
	private String value;
}
