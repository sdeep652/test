package com.tcts.foresight.problemnotification.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "problem_notification_criteria_configuration")
public class ProblemNotificationCriteriaEntity {
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_notification_criteria_configuration_seqgenerator")
	@SequenceGenerator(name = "problem_notification_criteria_configuration_seqgenerator", sequenceName = "problem_notification_criteria_configuration_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "no_of_incidents")
	private String noOfIncidents;
	
	@Column(name = "notification_rule_name")
	private String notificationRuleName;
	
	@Column(name = "rule_creation_date")
	private String ruleCreationDate;
	
	@Column(name = "is_active")
	private String isActive;
	
	
	@Column(name = "time_in_days")
	private String timeInDays;
	
	@Column(name = "time_in_hours")
	private String timeInHours;
	
	@Column(name = "time_in_mins")
	private String timeInMins;
	
	@Column(name = "time_in_secs")
	private String timeInSecs;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "problem_config_id", referencedColumnName = "id", nullable = false)
	private List<ProblemConfigurationActionsEntity> problemConfigurationActionsEntityList;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNoOfIncidents() {
		return noOfIncidents;
	}


	public void setNoOfIncidents(String noOfIncidents) {
		this.noOfIncidents = noOfIncidents;
	}


	public String getNotificationRuleName() {
		return notificationRuleName;
	}


	public void setNotificationRuleName(String notificationRuleName) {
		this.notificationRuleName = notificationRuleName;
	}


	public String getRuleCreationDate() {
		return ruleCreationDate;
	}


	public void setRuleCreationDate(String ruleCreationDate) {
		this.ruleCreationDate = ruleCreationDate;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public String getTimeInDays() {
		return timeInDays;
	}


	public void setTimeInDays(String timeInDays) {
		this.timeInDays = timeInDays;
	}


	public String getTimeInHours() {
		return timeInHours;
	}


	public void setTimeInHours(String timeInHours) {
		this.timeInHours = timeInHours;
	}


	public String getTimeInMins() {
		return timeInMins;
	}


	public void setTimeInMins(String timeInMins) {
		this.timeInMins = timeInMins;
	}


	public String getTimeInSecs() {
		return timeInSecs;
	}


	public void setTimeInSecs(String timeInSecs) {
		this.timeInSecs = timeInSecs;
	}


	public List<ProblemConfigurationActionsEntity> getProblemConfigurationActionsEntityList() {
		return problemConfigurationActionsEntityList;
	}


	public void setProblemConfigurationActionsEntityList(
			List<ProblemConfigurationActionsEntity> problemConfigurationActionsEntityList) {
		this.problemConfigurationActionsEntityList = problemConfigurationActionsEntityList;
	}


	@Override
	public String toString() {
		return "ProblemNotificationCriteriaEntity [id=" + id + ", noOfIncidents=" + noOfIncidents
				+ ", notificationRuleName=" + notificationRuleName + ", ruleCreationDate=" + ruleCreationDate
				+ ", isActive=" + isActive + ", timeInDays=" + timeInDays + ", timeInHours=" + timeInHours
				+ ", timeInMins=" + timeInMins + ", timeInSecs=" + timeInSecs
				+ ", problemConfigurationActionsEntityList=" + problemConfigurationActionsEntityList + "]";
	}



	
	
}
