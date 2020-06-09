package com.tcts.foresight.entity.problem;

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
@Table(name = "problem_history")
public class ProblemHistoryEntity implements Comparable<ProblemHistoryEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_history_seq_generator")
	@SequenceGenerator(name = "problem_history_seq_generator", sequenceName = "problem_history_seq", allocationSize = 1)
	@Column(name = "history_id")
	private Long historyID;

	public Long getHistoryID() {
		return historyID;
	}

	public void setHistoryID(Long historyID) {
		this.historyID = historyID;
	}

	@Column(name = "ticket_id")
	private String problemID;

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
	private List<ProblemHistoryUpdatedValues> problemHistoryUpdatedValuesList;

	public String getProblemID() {
		return problemID;
	}

	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public List<ProblemHistoryUpdatedValues> getProblemHistoryUpdatedValuesList() {
		return problemHistoryUpdatedValuesList;
	}

	public void setProblemHistoryUpdatedValuesList(List<ProblemHistoryUpdatedValues> problemHistoryUpdatedValuesList) {
		this.problemHistoryUpdatedValuesList = problemHistoryUpdatedValuesList;
	}

	@Override
	public String toString() {
		return "ProblemHistoryEntity [historyID=" + historyID + ", problemID=" + problemID + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", comments=" + comments + ", fieldName=" + fieldName + ", oldValue="
				+ oldValue + ", newValue=" + newValue + ", milestone=" + milestone + ", timeTaken=" + timeTaken
				+ ", problemHistoryUpdatedValuesList=" + problemHistoryUpdatedValuesList + "]";
	}

	@Override
	public int compareTo(ProblemHistoryEntity arg0) {
		// TODO Auto-generated method stub
		return arg0.getHistoryID().compareTo(getHistoryID());
	}
}
