package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "incident_history")
public class IncidentHistoryEntity implements Comparable<IncidentHistoryEntity>{

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incident_history_seq_generator")
	@SequenceGenerator(name="incident_history_seq_generator", sequenceName = "incident_history_seq", allocationSize = 1)
	@Column(name = "history_id")
	private Long historyID;
	

	public Long getHistoryID() {
		return historyID;
	}

	public void setHistoryID(Long historyID) {
		this.historyID = historyID;
	}

	@Column(name = "ticket_id")
	private String incidentID;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name = "comments")
	private String comments;

	@Column(name = "field_name")
	private String fieldName;
	
	@Column(name = "old_value")
	private String oldValue;

	@Column(name = "new_value")
	private String newValue;

	@Column(name = "milestone")
	private String milestone;
	
	@Column(name = "time_taken")
	private Long timeTaken;
	
	@Transient
	private List<IncidentHistoryUpdatedValues> incidentHistoryUpdatedValuesList;
		
	public List<IncidentHistoryUpdatedValues> getIncidentHistoryUpdatedValuesList() {
		return incidentHistoryUpdatedValuesList;
	}

	public void setIncidentHistoryUpdatedValuesList(List<IncidentHistoryUpdatedValues> incidentHistoryUpdatedValuesList) {
		this.incidentHistoryUpdatedValuesList = incidentHistoryUpdatedValuesList;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

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
	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	
	public Long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}


	@Override
	public String toString() {
		return "IncidentHistoryEntity [historyID=" + historyID + ", incidentID=" + incidentID + ", updatedBy="
				+ updatedBy + ", updatedDate=" + updatedDate + ", comments=" + comments + ", fieldName=" + fieldName
				+ ", oldValue=" + oldValue + ", newValue=" + newValue + ", milestone=" + milestone + ", timeTaken="
				+ timeTaken + ", incidentHistoryUpdatedValuesList=" + incidentHistoryUpdatedValuesList + "]";
	}

	@Override
	public int compareTo(IncidentHistoryEntity arg0) {
		// TODO Auto-generated method stub
		return arg0.getHistoryID().compareTo(getHistoryID());
	}

	

}
