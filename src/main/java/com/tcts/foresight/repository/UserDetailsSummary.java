package com.tcts.foresight.repository;

public interface UserDetailsSummary {
	
	public Long getId();
	public String getUserId();
	public String getFirstName();
	public String getLastName();
	public String getEmail();
	public String getContactNo();	
	default String getFullName() {
	    return getFirstName().concat(" ").concat(getLastName());
	  }

}
