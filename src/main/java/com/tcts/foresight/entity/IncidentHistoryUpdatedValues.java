package com.tcts.foresight.entity;

import javax.persistence.Transient;

import com.tcts.foresight.util.StringUtil;

public class IncidentHistoryUpdatedValues {

	
	private String fieldName;
	
	@Transient
	private String displayFieldName;

	private String oldValue;

	private String newValue;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	
	public String getDisplayFieldName() {
		
		displayFieldName = StringUtil.splitCamelCase(this.fieldName);
		return displayFieldName;
	}

	public void setDisplayFieldName(String displayFieldName) {
		this.displayFieldName = displayFieldName;
	}
	
	
	
}
