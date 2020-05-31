package com.simulation.dice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.simulation.dice.domain.Simulation;
import com.simulation.dice.domain.SimulationAggregation;
import com.simulation.dice.exceptions.DiceApiException;
import com.simulation.dice.exceptions.ExceptionLayer;

@Service
public class AggregateSimulation implements AggregateSimulationRepository {
	
	@Autowired
    private MongoOperations mongoOperations;

	@Override
	public List<SimulationAggregation> findAggregate() throws DiceApiException {
		AggregationResults<SimulationAggregation> orderAggregate = null;
		try {
			ProjectionOperation projectStage = Aggregation.project("diceDistribution")
					.andInclude("simulationDate", "result","rollsCount").andExclude("_id");
			GroupOperation group = Aggregation.group("diceDistribution.sides", "diceDistribution.dice")
					.sum("diceDistribution.rolls").as("rolls").push("rollsCount").as("rollsCounts")
					.push("diceDistribution.dice").as("dice").push("diceDistribution.sides").as("sides");
			 Aggregation aggregate = Aggregation.newAggregation(projectStage, group);
			  orderAggregate = mongoOperations.aggregate(aggregate, Simulation.class, SimulationAggregation.class);
		} catch (Exception e) {
			throw DiceApiException.builder().errorMessage(e.getMessage()).layer(ExceptionLayer.REPOSITORY.getValue()).build();
		}
  return orderAggregate.getMappedResults();
	}
	
	@Override
	public SimulationAggregation findAggregateByDiceAndSide(int dice, int side) throws DiceApiException {
		AggregationResults<SimulationAggregation> orderAggregate = null;
		try {
			MatchOperation matchStage = Aggregation.match(new Criteria("diceDistribution.dice").is(dice).and("diceDistribution.sides").is(side));
			ProjectionOperation projectStage = Aggregation.project("diceDistribution")
					.andInclude("simulationDate", "result","rollsCount").andExclude("_id");
			GroupOperation group = Aggregation.group("diceDistribution.sides", "diceDistribution.dice")
					.sum("diceDistribution.rolls").as("rolls").push("rollsCount").as("rollsCounts")
					.push("diceDistribution.dice").as("dice").push("diceDistribution.sides").as("sides");
			 Aggregation aggregate = Aggregation.newAggregation(projectStage, matchStage, group);
			  orderAggregate = mongoOperations.aggregate(aggregate, Simulation.class, SimulationAggregation.class);
		} catch (Exception e) {
			throw DiceApiException.builder().errorMessage(e.getMessage()).layer(ExceptionLayer.REPOSITORY.getValue()).build();
		}
		if(orderAggregate.getMappedResults().size() < 1) {
			return new SimulationAggregation();			
		}else if(orderAggregate.getMappedResults().size() > 1) {
			throw new DiceApiException("Multiple Relative Distributions for dice number " + dice +
					"and sides " + side);
		}
  return orderAggregate.getMappedResults().get(0);
	}

}
