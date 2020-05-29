package com.simulation.dice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.simulation.dice.domain.Simulation;

@Repository
public interface SimulationRepository extends MongoRepository<Simulation, String>{

	Optional<Simulation> findById(String id);
	List<Simulation> findAll();
}
