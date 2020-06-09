package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(value = Include.NON_NULL)
@Table(name = "problem_notification_details_incident")
public class ProblemNotificationIncident {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_notification_details_incident_seq_generator")
	@SequenceGenerator(name="problem_notification_details_incident_seq_generator", sequenceName = "problem_notification_details_incident_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "incident_id")
	private String incidentID;
	
	@Column(name = "incident_creation_date")
	private String incidentCreationDate;

	@Transient
	private String priority;
	
	@Transient
	private String category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProblemNotificationIncident [id=" + id + ", incidentID=" + incidentID + ", priority=" + priority
				+ ", category=" + category + "]";
	}

	public String getIncidentCreationDate() {
		return incidentCreationDate;
	}

	public void setIncidentCreationDate(String incidentCreationDate) {
		this.incidentCreationDate = incidentCreationDate;
	}

	
	
	
	
}
