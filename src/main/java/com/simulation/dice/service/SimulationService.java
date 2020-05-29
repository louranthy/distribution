package com.simulation.dice.service;

import java.util.List;

import com.simulation.dice.domain.Simulation;
import com.simulation.dice.domain.SimulationAggregation;
import com.simulation.dice.domain.SimulationAggregationResponseWrapper;

public interface SimulationService  {

	public void saveSimulationResult(Simulation simulation);
	public List<Simulation> getAllSimulations();
	public List<SimulationAggregationResponseWrapper> findAggregate() throws Exception;
}
