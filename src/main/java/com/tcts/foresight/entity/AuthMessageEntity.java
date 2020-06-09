package com.tcts.foresight.entity;

import javax.persistence.Transient;

public class AuthMessageEntity {
	
	@Transient
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
