package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sla_condtion_values")
public class SLAConditionValuesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sla_condtion_values_seq_generator")
	@SequenceGenerator(name="sla_condtion_values_seq_generator", sequenceName = "sla_condtion_values_seq", allocationSize = 1)
	@Column(name ="id")
	private Long id;
	
	@Column(name="condition_column_value")
	private String conditionColumnValue;
	
	@Column(name="condition_display_value")
	private String conditionDisplayValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConditionColumnValue() {
		return conditionColumnValue;
	}

	public void setConditionColumnValue(String conditionColumnValue) {
		this.conditionColumnValue = conditionColumnValue;
	}

	public String getConditionDisplayValue() {
		return conditionDisplayValue;
	}

	public void setConditionDisplayValue(String conditionDisplayValue) {
		this.conditionDisplayValue = conditionDisplayValue;
	}

	
	
}
