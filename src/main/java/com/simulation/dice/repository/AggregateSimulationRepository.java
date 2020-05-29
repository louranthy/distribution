package com.simulation.dice.repository;

import java.util.List;

import com.simulation.dice.domain.SimulationAggregation;
import com.simulation.dice.exceptions.DiceApiException;


public interface AggregateSimulationRepository  {
	
	List<SimulationAggregation> findAggregate() throws  DiceApiException;
}
