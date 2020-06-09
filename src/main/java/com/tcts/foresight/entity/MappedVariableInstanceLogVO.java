package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "mapped_variable_instance_log")
public class MappedVariableInstanceLogVO implements Cloneable {

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Column(name = "parent_ticketid")
	private String parentTicketId;
	@Transient
	String childTicketId;
	
	@Transient
	String totalIncidentAgeingInMinutes;

	@Id
	@Column(name = "processinstance_id")
	private String processInstanceId;

	@Column(name = "ticket_id")
	private String incidentID;
	
	@Column(name = "incident_type")
	private String incidentType;

	@Transient
	private String atm;

	@Column(name = "addtokb")
	private String addToKb;

	@Column(name = "resolution_method")
	private String resolutionMethod;

	@Column(name = "incident_closed_date")
	private String incidentClosedDate;

	@Column(name = "resolvedby")
	private String resolvedBy;

	@Column(name = "resolution_type")
	private String resolutionType;

	@Column(name = "resolution_remarks")
	private String resolutionRemarks;

	@Column(name = "resolved_timer")
	private String resolvedTimer;

	@Column(name = "category")
	private String category;

	@Column(name = "sub_category")
	private String subCategory;

	@Column(name = "title")
	private String title;

	@Column(name = "impact")
	private String impact;

	@Column(name = "urgency")
	private String urgency;

	@Column(name = "priority")
	private String priority;

	@Column(name = "source")
	private String source;

	@Column(name = "source_contact")
	private String sourceContact;

	@Column(name = "group_name")
	private String assignmentGroup;

	@Column(name = "assign_to")
	private String assignTo;

	@Column(name = "major_incident")
	private String markAsMajorIncident;

	@Column(name = "configuration_item")
	private String configurationItem;

	@Column(name = "comments")
	private String comments;

	@Column(name = "descriptions")
	private String descriptions;

	@Column(name = "attachment")
	private String addAttachment;

	@Column(name = "status")
	private String status;

	@Column(name = "status_remark")
	private String statusRemark;

	@Column(name = "created_date")
	private String incidentCreationDate;

	@Column(name = "resolved_date")
	private String resolvedDate;

	@Column(name = "last_updated_date")
	private String lastUpdatedDate;

	@Column(name = "last_updated_By")
	private String lastUpdatedBy;

	@Column(name = "module")
	private String module;

	@Column(name = "fullname")
	private String fullName;

	@Column(name = "initiator")
	private String initiator;

	@Column(name = "createdby_full_name")
	private String createdByFullName;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name="response_sla_breach")
	private String responseSlaBreach;
	
	@Column(name="resolution_sla_breach")
	private String resolutionSlaBreach;
	
	@Column(name="resolution_sla_bracket")
	private String resolutionSlaBracket;
	
	@Column(name="email")
	private String email;
	
	@Column(name="sms")
	private String sms;
	
	@Column(name="status_change_checkbox")
	private String statusChangeCheckbox;
					 
	
	@Column(name="feedback_checkbox")
	private String feedbackCheckbox;
	
	@Column(name = "work_around")
	private String workAround;
	
	
	public String getResolutionSlaBracket() {
		return resolutionSlaBracket;
	}

	public void setResolutionSlaBracket(String resolutionSlaBracket) {
		this.resolutionSlaBracket = resolutionSlaBracket;
	}

	@Transient
	private String loggedInUser;
	
	@Transient
	private Long incidentAgeing;

	

	@Transient
	private List<IncidentAttachmentEntity> incidentAttachList;

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceContact() {
		return sourceContact;
	}

	public void setSourceContact(String sourceContact) {
		this.sourceContact = sourceContact;
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

	public String getMarkAsMajorIncident() {
		return markAsMajorIncident;
	}

	public void setMarkAsMajorIncident(String markAsMajorIncident) {
		this.markAsMajorIncident = markAsMajorIncident;
	}

	public String getConfigurationItem() {
		return configurationItem;
	}

	public void setConfigurationItem(String configurationItem) {
		this.configurationItem = configurationItem;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getAddAttachment() {
		return addAttachment;
	}

	public void setAddAttachment(String addAttachment) {
		this.addAttachment = addAttachment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getIncidentCreationDate() {
		return incidentCreationDate;
	}

	public void setIncidentCreationDate(String incidentCreationDate) {
		this.incidentCreationDate = incidentCreationDate;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public String getAtm() {
		return atm;
	}

	public void setAtm(String atm) {
		this.atm = atm;
	}

	public String getAddToKb() {
		return addToKb;
	}

	public void setAddToKb(String addToKb) {
		this.addToKb = addToKb;
	}

	public String getResolutionMethod() {
		return resolutionMethod;
	}

	public void setResolutionMethod(String resolutionMethod) {
		this.resolutionMethod = resolutionMethod;
	}

	public String getIncidentClosedDate() {
		return incidentClosedDate;
	}

	public void setIncidentClosedDate(String incidentClosedDate) {
		this.incidentClosedDate = incidentClosedDate;
	}

	public String getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(String resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public String getResolutionType() {
		return resolutionType;
	}

	public void setResolutionType(String resolutionType) {
		this.resolutionType = resolutionType;
	}

	public String getResolutionRemarks() {
		return resolutionRemarks;
	}

	public void setResolutionRemarks(String resolutionRemarks) {
		this.resolutionRemarks = resolutionRemarks;
	}

	public String getResolvedTimer() {
		return resolvedTimer;
	}

	public void setResolvedTimer(String resolvedTimer) {
		this.resolvedTimer = resolvedTimer;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public List<IncidentAttachmentEntity> getIncidentAttachList() {
		return incidentAttachList;
	}

	public void setIncidentAttachList(List<IncidentAttachmentEntity> incidentAttachList) {
		this.incidentAttachList = incidentAttachList;
	}

	public String getParentTicketId() {
		return parentTicketId;
	}

	public void setParentTicketId(String parentTicketId) {
		this.parentTicketId = parentTicketId;
	}

	public String getChildTicketId() {
		return childTicketId;
	}

	public void setChildTicketId(String childTicketId) {
		this.childTicketId = childTicketId;
	}

	public String getResponseSlaBreach() {
		return responseSlaBreach;
	}

	public void setResponseSlaBreach(String responseSlaBreach) {
		this.responseSlaBreach = responseSlaBreach;
	}

	public String getResolutionSlaBreach() {
		return resolutionSlaBreach;
	}

	public void setResolutionSlaBreach(String resolutionSlaBreach) {
		this.resolutionSlaBreach = resolutionSlaBreach;
	}



	public Long getIncidentAgeing() {
		return incidentAgeing;
	}

	public void setIncidentAgeing(Long incidentAgeing) {
		this.incidentAgeing = incidentAgeing;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getStatusChangeCheckbox() {
		return statusChangeCheckbox;
	}

	public void setStatusChangeCheckbox(String statusChangeCheckbox) {
		this.statusChangeCheckbox = statusChangeCheckbox;
	}

	public String getFeedbackCheckbox() {
		return feedbackCheckbox;
	}

	public void setFeedbackCheckbox(String feedbackCheckbox) {
		this.feedbackCheckbox = feedbackCheckbox;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	

	public String getWorkAround() {
		return workAround;
	}

	public void setWorkAround(String workAround) {
		this.workAround = workAround;
	}

	public String getTotalIncidentAgeingInMinutes() {
		return totalIncidentAgeingInMinutes;
	}

	public void setTotalIncidentAgeingInMinutes(String totalIncidentAgeingInMinutes) {
		this.totalIncidentAgeingInMinutes = totalIncidentAgeingInMinutes;
	}

	@Override
	public String toString() {
		return "MappedVariableInstanceLogVO [parentTicketId=" + parentTicketId + ", childTicketId=" + childTicketId
				+ ", totalIncidentAgeingInMinutes=" + totalIncidentAgeingInMinutes + ", processInstanceId="
				+ processInstanceId + ", incidentID=" + incidentID + ", incidentType=" + incidentType + ", atm=" + atm
				+ ", addToKb=" + addToKb + ", resolutionMethod=" + resolutionMethod + ", incidentClosedDate="
				+ incidentClosedDate + ", resolvedBy=" + resolvedBy + ", resolutionType=" + resolutionType
				+ ", resolutionRemarks=" + resolutionRemarks + ", resolvedTimer=" + resolvedTimer + ", category="
				+ category + ", subCategory=" + subCategory + ", title=" + title + ", impact=" + impact + ", urgency="
				+ urgency + ", priority=" + priority + ", source=" + source + ", sourceContact=" + sourceContact
				+ ", assignmentGroup=" + assignmentGroup + ", assignTo=" + assignTo + ", markAsMajorIncident="
				+ markAsMajorIncident + ", configurationItem=" + configurationItem + ", comments=" + comments
				+ ", descriptions=" + descriptions + ", addAttachment=" + addAttachment + ", status=" + status
				+ ", statusRemark=" + statusRemark + ", incidentCreationDate=" + incidentCreationDate
				+ ", resolvedDate=" + resolvedDate + ", lastUpdatedDate=" + lastUpdatedDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", module=" + module + ", fullName=" + fullName + ", initiator=" + initiator
				+ ", createdByFullName=" + createdByFullName + ", createdBy=" + createdBy + ", responseSlaBreach="
				+ responseSlaBreach + ", resolutionSlaBreach=" + resolutionSlaBreach + ", resolutionSlaBracket="
				+ resolutionSlaBracket + ", email=" + email + ", sms=" + sms + ", statusChangeCheckbox="
				+ statusChangeCheckbox + ", feedbackCheckbox=" + feedbackCheckbox + ", workAround=" + workAround
				+ ", loggedInUser=" + loggedInUser + ", incidentAgeing=" + incidentAgeing + ", incidentAttachList="
				+ incidentAttachList + "]";
	}

	

	

	

	

	
	
	

}
