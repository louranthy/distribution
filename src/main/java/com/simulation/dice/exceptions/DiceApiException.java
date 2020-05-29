package com.simulation.dice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder =  true)
public class DiceApiException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String layer;
	public String errorMessage;
	
	
	public DiceApiException(String errorMessage) {
        super(errorMessage);
    }
	

}
