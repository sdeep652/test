package com.tcts.foresight.problemnotification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "problem_notification_configuration_actions")
public class ProblemConfigurationActionsEntity implements Comparable<ProblemConfigurationActionsEntity>{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_notification_configuration_actions_seqgenerator")
	@SequenceGenerator(name = "problem_notification_configuration_actions_seqgenerator", sequenceName = "problem_notification_configuration_actions_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "operator")
	private String operator;

	@Column(name = "condition_on")
	private String conditionOn;

	@Column(name = "value")
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getConditionOn() {
		return conditionOn;
	}

	public void setConditionOn(String conditionOn) {
		this.conditionOn = conditionOn;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(ProblemConfigurationActionsEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
