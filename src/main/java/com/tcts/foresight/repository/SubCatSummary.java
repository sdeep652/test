package com.tcts.foresight.repository;

import javax.persistence.OrderBy;

public interface SubCatSummary {
	
	@OrderBy
	public Long getId();
	public String getName();

}
