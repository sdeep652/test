package com.tcts.foresight.entity.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "problem_incident_mapping")
public class ProblemIncidentEntity {

	@Id
	@GeneratedValue(generator = "problem_incident_seq_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "problem_incident_seq_generator", allocationSize = 1, sequenceName = "problem_incident_seq")
	private Long Id;

	@Column(name = "problem_ticket_id")
	private String problemID;

	@Column(name = "incident_ticket_id")
	private String incidentID;

	@Column(name = "relation_type")
	private String relationType;

	@Column(name = "incident_category")
	private String incidentCategory;

	@Column(name = "incident_priority")
	private String incidentPriority;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getProblemID() {
		return problemID;
	}

	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getIncidentCategory() {
		return incidentCategory;
	}

	public void setIncidentCategory(String incidentCategory) {
		this.incidentCategory = incidentCategory;
	}

	public String getIncidentPriority() {
		return incidentPriority;
	}

	public void setIncidentPriority(String incidentPriority) {
		this.incidentPriority = incidentPriority;
	}

}
