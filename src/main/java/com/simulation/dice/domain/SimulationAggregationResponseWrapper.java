package com.simulation.dice.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulationAggregationResponseWrapper {

	private int rolls;
	private int max;
	private int dice;
	private int sides;
	private Map<Integer, Float> relativeDistribution;

}
