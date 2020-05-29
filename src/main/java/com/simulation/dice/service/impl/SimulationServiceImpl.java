package com.simulation.dice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulation.dice.domain.Sequence;
import com.simulation.dice.domain.Simulation;
import com.simulation.dice.domain.SimulationAggregation;
import com.simulation.dice.domain.SimulationAggregationResponseWrapper;
import com.simulation.dice.exceptions.DiceApiException;
import com.simulation.dice.repository.AggregateSimulation;
import com.simulation.dice.repository.SimulationRepository;
import com.simulation.dice.service.SequenceGenerator;
import com.simulation.dice.service.SimulationService;

@Service
public class SimulationServiceImpl implements SimulationService {

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	private SimulationRepository simulationRepository;

	@Autowired
	private AggregateSimulation aggregateSimulation;

	@Override
	public void saveSimulationResult(Simulation simulation) {
		simulation.setId(sequenceGenerator.getNextSequenceId(Sequence.COLLECTION_NAME).toString());
		simulationRepository.save(simulation);

	}

	@Override
	public List<Simulation> getAllSimulations() {
		return simulationRepository.findAll();
	}

	@Override
	public List<SimulationAggregationResponseWrapper> findAggregate() throws DiceApiException {
		return computeRelativeDistribution(aggregateSimulation.findAggregate());
	}

	private List<SimulationAggregationResponseWrapper> computeRelativeDistribution(
			List<SimulationAggregation> simulationAggregations) {
		List<SimulationAggregationResponseWrapper> responseWrappers = new ArrayList<SimulationAggregationResponseWrapper>();
		try {
		for (SimulationAggregation simAggregation : simulationAggregations) {
			SimulationAggregationResponseWrapper responseWrapper = SimulationAggregationResponseWrapper.builder()
					.max(simAggregation.getDice() * simAggregation.getSides()).rolls(simAggregation.getRolls()).build();
			Map<Integer, AtomicInteger> sumOfMaps = new ConcurrentHashMap<Integer, AtomicInteger>();
			for (int i = 0; i < simAggregation.getRollsCounts().size(); i++) {
				for (int j = 1; j <= simAggregation.getDice() * simAggregation.getSides(); j++) {
					sumOfMaps.putIfAbsent(j, new AtomicInteger(0));
					Map<Integer, AtomicInteger> aggregate = simAggregation.getRollsCounts().get(i);
					aggregate.putIfAbsent(j, new AtomicInteger(0));
					AtomicInteger value = aggregate.get(j);
					sumOfMaps.get(j).addAndGet(value.get());
				}
				sumOfMaps.values().removeIf(f -> f.intValue() == 0);
			}
			Map<Integer, Float> percentage = new ConcurrentHashMap<Integer, Float>();
			sumOfMaps.entrySet().stream()
		      .forEach(e -> percentage.put(e.getKey(), (e.getValue().floatValue()/simAggregation.getRolls()) * 100));
			responseWrapper.setRelativeDistribution(percentage);
			responseWrappers.add(responseWrapper);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return responseWrappers;
	}

}
