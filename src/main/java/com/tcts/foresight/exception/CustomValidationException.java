package com.tcts.foresight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class CustomValidationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public CustomValidationException(String validationMessage) {
		super(validationMessage);
	}

}
