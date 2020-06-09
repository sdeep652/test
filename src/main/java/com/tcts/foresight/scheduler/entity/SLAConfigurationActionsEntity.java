package com.tcts.foresight.scheduler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sla_configuration_actions")
public class SLAConfigurationActionsEntity implements Comparable<SLAConfigurationActionsEntity>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sla_config_actions_seq_generator")
	@SequenceGenerator(name = "sla_config_actions_seq_generator", sequenceName = "sla_config_actions_seq", allocationSize = 1)
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
	public int compareTo(SLAConfigurationActionsEntity o) {
		// TODO Auto-generated method stub
		return o.getId().compareTo(getId());
	}

	@Override
	public String toString() {
		return "SLAConfigurationActionsEntity [id=" + id + ", operator=" + operator + ", conditionOn=" + conditionOn
				+ ", value=" + value + "]";
	}
	
	
	
}
