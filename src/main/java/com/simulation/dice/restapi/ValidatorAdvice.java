package com.simulation.dice.restapi;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simulation.dice.domain.SimulationResponseWrapper;
import com.simulation.dice.domain.SimulationStatus;

@ControllerAdvice
public class ValidatorAdvice {
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<SimulationResponseWrapper> handleValidationException(ConstraintViolationException e){
		Map<String, String> invalidMessages = new HashMap<>();
		for(ConstraintViolation<?> s:e.getConstraintViolations()){
			invalidMessages.put(s.getPropertyPath().toString(), s.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimulationResponseWrapper.builder().validationMessages(invalidMessages)
				.simulationStatus(SimulationStatus.INVALID_INPUTS.getValue()).build());
	}
}
