package com.tcts.foresight.dao;

public class NotificationReplaceVO {

	private String incidentID = "";
	private String status = "";
	private String createdBy="";
	private String priority="";
	private String assignmentGroup="";
	private String assignTo ="";
	private String createdDate="";
	
	
	private String oldStatus="";
	private String newStatus="";
	private String action="";
	private String percentageSLAElapsed="";
	private String slaType="";
	private String feedbackLink="";
	
	
	
	private String emailIdList ="";
	private String phoneNumberList="";
	
	
	public String getIncidentID() {
		return incidentID;
	}
	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getAssignmentGroup() {
		return assignmentGroup;
	}
	public void setAssignmentGroup(String assignmentGroup) {
		this.assignmentGroup = assignmentGroup;
	}
	public String getAssignTo() {
		return assignTo;
	}
	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getSlaType() {
		return slaType;
	}
	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}
	public String getEmailIdList() {
		return emailIdList;
	}
	public void setEmailIdList(String emailIdList) {
		this.emailIdList = emailIdList;
	}
	public String getPhoneNumberList() {
		return phoneNumberList;
	}
	public void setPhoneNumberList(String phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}
	public String getPercentageSLAElapsed() {
		return percentageSLAElapsed;
	}
	public void setPercentageSLAElapsed(String percentageSLAElapsed) {
		this.percentageSLAElapsed = percentageSLAElapsed;
	}
	public String getFeedbackLink() {
		return feedbackLink;
	}
	public void setFeedbackLink(String feedbackLink) {
		this.feedbackLink = feedbackLink;
	}
	
	
}
