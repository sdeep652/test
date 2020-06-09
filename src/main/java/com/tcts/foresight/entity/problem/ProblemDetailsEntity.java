package com.tcts.foresight.entity.problem;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "problem_details")
public class ProblemDetailsEntity implements Cloneable {

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Column(name = "problem_ticket_id")
	private String problemID;

	@Id
	@Column(name = "processinstance_id")
	private String processInstanceId;

	@Column(name = "created_date")
	private String problemCreationDate;

	@Column(name = "category")
	private String category;

	@Column(name = "sub_category")
	private String subCategory;

	@Column(name = "title")
	private String title;

	@Column(name = "descriptions")
	private String descriptions;

	@Column(name = "urgency")
	private String urgency;

	@Column(name = "impact")
	private String impact;

	@Column(name = "priority")
	private String priority;

	@Column(name = "source")
	private String source;

	@Column(name = "source_contact")
	private String sourceContact;

	@Column(name = "configuration_item")
	private String configurationItem;

	@Column(name = "assignment_group")
	private String assignmentGroup;

	@Column(name = "assign_to")
	private String assignTo;

	@Column(name = "comments")
	private String comments;

	@Column(name = "major_problem")
	private String markAsMajorProblem;

	@Column(name = "status")
	private String status;

	@Column(name = "module")
	private String module;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_by_full_name")
	private String createdByFullName;

	@Column(name = "status_remark")
	private String statusRemark;

	@Transient
	private String atm;

	@Column(name = "last_updated_date")
	private String lastUpdatedDate;

	@Column(name = "resolved_date")
	private String resolvedDate;

	@Column(name = "resolved_timer")
	private String resolvedTimer;

	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

	@Column(name = "rca")
	private String rca;
	
	@Column(name = "resolved_by")
	private String resolvedBy;
	
	@Column(name = "resolution_type")
	private String resolutionType;
	
	@Column(name = "addtokb")
	private String addToKb ;
	
	@Column(name = "resolution_remarks")
	private String resolutionRemarks;
	
	@Column(name = "major_do_well")
	private String majorDoWell ;
	
	@Column(name = "major_been_better")
	private String majorBeenBetter;
	
	@Column(name = "major_prevent_reoccurrence")
	private String majorPreventReoccurrence;
	
	@Column(name = "major_dependencies")
	private String majorDependencies;
	
	@JsonIgnore
	@Column(name = "initiator")
	private String initiator;
	
	@Column(name = "work_around")
	private String workAround;
	
	@Transient
	private String isProblemReviewSubmitted;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "add_kedb")
	private String addKedb;

	@Column(name = "broadcast_workaround")
	private String broadcastWorkAround;

	@Transient
	private List<ProblemAttachmentEntity> problemAttachmentList;

	@Transient
	private List<ProblemIncidentEntity> associatedIncidentList;
	
	@Column(name = "known_error_title")
	private String knownErrorTitle;

	@Column(name = "known_error_description")
	private String knownErrorDescription;
	

	public List<ProblemAttachmentEntity> getProblemAttachmentList() {
		return problemAttachmentList;
	}

	public void setProblemAttachmentList(List<ProblemAttachmentEntity> problemAttachmentList) {
		this.problemAttachmentList = problemAttachmentList;
	}

	public String getProblemID() {
		return problemID;
	}

	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProblemCreationDate() {
		return problemCreationDate;
	}

	public void setProblemCreationDate(String problemCreationDate) {
		this.problemCreationDate = problemCreationDate;
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

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
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

	public String getConfigurationItem() {
		return configurationItem;
	}

	public void setConfigurationItem(String configurationItem) {
		this.configurationItem = configurationItem;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMarkAsMajorProblem() {
		return markAsMajorProblem;
	}

	public void setMarkAsMajorProblem(String markAsMajorProblem) {
		this.markAsMajorProblem = markAsMajorProblem;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getAtm() {
		return atm;
	}

	public void setAtm(String atm) {
		this.atm = atm;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getResolvedTimer() {
		return resolvedTimer;
	}

	public void setResolvedTimer(String resolvedTimer) {
		this.resolvedTimer = resolvedTimer;
	}

	public String getIsProblemReviewSubmitted() {
		return isProblemReviewSubmitted;
	}

	public void setIsProblemReviewSubmitted(String isProblemReviewSubmitted) {
		this.isProblemReviewSubmitted = isProblemReviewSubmitted;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public List<ProblemIncidentEntity> getAssociatedIncidentList() {
		return associatedIncidentList;
	}

	public void setAssociatedIncidentList(List<ProblemIncidentEntity> associatedIncidentList) {
		this.associatedIncidentList = associatedIncidentList;
	}

	public String getRca() {
		return rca;
	}

	public void setRca(String rca) {
		this.rca = rca;
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

	public String getAddToKb() {
		return addToKb;
	}

	public void setAddToKb(String addToKb) {
		this.addToKb = addToKb;
	}

	public String getResolutionRemarks() {
		return resolutionRemarks;
	}

	public void setResolutionRemarks(String resolutionRemarks) {
		this.resolutionRemarks = resolutionRemarks;
	}

	public String getMajorDoWell() {
		return majorDoWell;
	}

	public void setMajorDoWell(String majorDoWell) {
		this.majorDoWell = majorDoWell;
	}

	public String getMajorBeenBetter() {
		return majorBeenBetter;
	}

	public void setMajorBeenBetter(String majorBeenBetter) {
		this.majorBeenBetter = majorBeenBetter;
	}

	public String getMajorPreventReoccurrence() {
		return majorPreventReoccurrence;
	}

	public void setMajorPreventReoccurrence(String majorPreventReoccurrence) {
		this.majorPreventReoccurrence = majorPreventReoccurrence;
	}

	public String getMajorDependencies() {
		return majorDependencies;
	}

	public void setMajorDependencies(String majorDependencies) {
		this.majorDependencies = majorDependencies;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getWorkAround() {
		return workAround;
	}

	public void setWorkAround(String workAround) {
		this.workAround = workAround;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddKedb() {
		return addKedb;
	}

	public void setAddKedb(String addKedb) {
		this.addKedb = addKedb;
	}

	public String getBroadcastWorkAround() {
		return broadcastWorkAround;
	}

	public void setBroadcastWorkAround(String broadcastWorkAround) {
		this.broadcastWorkAround = broadcastWorkAround;
	}

	public String getKnownErrorTitle() {
		return knownErrorTitle;
	}

	public void setKnownErrorTitle(String knownErrorTitle) {
		this.knownErrorTitle = knownErrorTitle;
	}

	public String getKnownErrorDescription() {
		return knownErrorDescription;
	}

	public void setKnownErrorDescription(String knownErrorDescription) {
		this.knownErrorDescription = knownErrorDescription;
	}

	@Override
	public String toString() {
		return "ProblemDetailsEntity [problemID=" + problemID + ", processInstanceId=" + processInstanceId
				+ ", problemCreationDate=" + problemCreationDate + ", category=" + category + ", subCategory="
				+ subCategory + ", title=" + title + ", descriptions=" + descriptions + ", urgency=" + urgency
				+ ", impact=" + impact + ", priority=" + priority + ", source=" + source + ", sourceContact="
				+ sourceContact + ", configurationItem=" + configurationItem + ", assignmentGroup=" + assignmentGroup
				+ ", assignTo=" + assignTo + ", comments=" + comments + ", markAsMajorProblem=" + markAsMajorProblem
				+ ", status=" + status + ", module=" + module + ", createdBy=" + createdBy + ", createdByFullName="
				+ createdByFullName + ", statusRemark=" + statusRemark + ", atm=" + atm + ", lastUpdatedDate="
				+ lastUpdatedDate + ", resolvedDate=" + resolvedDate + ", resolvedTimer=" + resolvedTimer
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", rca=" + rca + ", resolvedBy=" + resolvedBy
				+ ", resolutionType=" + resolutionType + ", addToKb=" + addToKb + ", resolutionRemarks="
				+ resolutionRemarks + ", majorDoWell=" + majorDoWell + ", majorBeenBetter=" + majorBeenBetter
				+ ", majorPreventReoccurrence=" + majorPreventReoccurrence + ", majorDependencies=" + majorDependencies
				+ ", initiator=" + initiator + ", workAround=" + workAround + ", isProblemReviewSubmitted="
				+ isProblemReviewSubmitted + ", fullName=" + fullName + ", addKedb=" + addKedb
				+ ", broadcastWorkAround=" + broadcastWorkAround + ", problemAttachmentList=" + problemAttachmentList
				+ ", associatedIncidentList=" + associatedIncidentList + ", knownErrorTitle=" + knownErrorTitle
				+ ", knownErrorDescription=" + knownErrorDescription + "]";
	}

	

	

	


	

}
