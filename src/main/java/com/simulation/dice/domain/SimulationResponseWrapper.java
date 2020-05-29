package com.simulation.dice.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SimulationResponseWrapper {
	
	private String simulationStatus;
	private Simulation simulation;
	private Map<String, String> validationMessages;

}
