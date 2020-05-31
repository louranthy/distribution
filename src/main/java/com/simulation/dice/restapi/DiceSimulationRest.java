package com.simulation.dice.restapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simulation.dice.domain.DiceDistribution;
import com.simulation.dice.domain.Simulation;
import com.simulation.dice.domain.SimulationAggregation;
import com.simulation.dice.domain.SimulationAggregationResponseWrapper;
import com.simulation.dice.domain.SimulationResponseWrapper;
import com.simulation.dice.domain.SimulationStatus;
import com.simulation.dice.service.SimulationService;

@RestController("dice")
@Validated
public class DiceSimulationRest {

	@Autowired
	private SimulationService simulationService;

	@GetMapping("/simulate/")
	ResponseEntity<SimulationResponseWrapper> simulate(@RequestParam @Min(1) int dice, @RequestParam @Min(4) int sides,
			@RequestParam @Min(1) int rolls) {
		Simulation simulation = new Simulation();
	
		try {
			
			DiceDistribution diceDistribution = DiceDistribution.builder().dice(dice).rolls(rolls).sides(sides).build();
			final Map<Integer, AtomicInteger> rollCountMap = new ConcurrentHashMap<Integer, AtomicInteger>();
			Random r = new Random();
			for (int i = 1; i <= rolls; i++) {
				int sum = 0;
				for (int j = 1; j <= dice; j++) {
					sum += r.nextInt(sides) + 1;
				}

				rollCountMap.putIfAbsent(sum, new AtomicInteger(0));
				rollCountMap.get(sum).incrementAndGet();

			}
			simulation = buildSimulationObject(diceDistribution, rollCountMap);
			simulationService.saveSimulationResult(simulation);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					SimulationResponseWrapper.builder().simulationStatus(SimulationStatus.ERROR.getValue()).build());
		}
		return ResponseEntity.ok().body(
				SimulationResponseWrapper.builder().simulation(simulation).
				simulationStatus(SimulationStatus.SUCCESS.getValue()).build());
	}

	@GetMapping("/getSimulations/")
	ResponseEntity<List<Simulation>> getAllSimulations() {
		List<Simulation> simulations = new ArrayList<Simulation>();
		try {
			simulations = simulationService.getAllSimulations();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simulations);
		}
		return ResponseEntity.ok().body(simulations);
	}
	
	@GetMapping("/getSimulationById/")
	ResponseEntity<Simulation> getAllSimulationById(@RequestParam String id) {
		Simulation simulation = new Simulation();
		try {
			simulation = simulationService.findById(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simulation);
		}
		return ResponseEntity.ok().body(simulation);
	}

	@GetMapping("/getAggregates/")
	ResponseEntity<List<SimulationAggregationResponseWrapper>> getAggregates() {
		List<SimulationAggregationResponseWrapper> simulations = new ArrayList<SimulationAggregationResponseWrapper>();
		try {
			simulations = simulationService.findAggregate();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simulations);
		}
		return ResponseEntity.ok().body(simulations);
	}

	private Simulation buildSimulationObject(DiceDistribution diceDistribution,
			Map<Integer, AtomicInteger> rollCountMap) {

		return Simulation.builder().diceDistribution(diceDistribution).rollsCount(rollCountMap)
				.result(SimulationStatus.SUCCESS.getValue()).simulationDate(new Date()).build();

	}
	
	@GetMapping("/getAggregatesByDiceAndSides/")
	ResponseEntity<SimulationAggregationResponseWrapper> getAggregatesByDiceAndSides(@RequestParam @Min(1) int dice, @RequestParam @Min(4) int sides) {
		SimulationAggregationResponseWrapper simulation = new SimulationAggregationResponseWrapper();
		try {
			simulation = simulationService.findAggregateByDiceAndSides(dice, sides);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simulation);
		}
		return ResponseEntity.ok().body(simulation);
	}
}
