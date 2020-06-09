package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "incident_template")
public class TemplateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incident_template_seq_generator")
	@SequenceGenerator(name = "incident_template_seq_generator", sequenceName = "incident_template_seq", allocationSize = 1)
	@Column(name = "template_id")
	private Long templateId;

	@Column(name = "template_name")
	private String tamplateName;

	@Column(name = "active")
	private String active;

	@Column(name = "publish_for_groups")
	private String publishForGroups;

	@Column(name = "category")
	private String category;

	@Column(name = "category_id")
	private String categoryId;

	@Column(name = "sub_category")
	private String subCategory;

	@Column(name = "sub_category_id")
	private String subCategoryId;

	@Column(name = "title")
	private String title;

	@Column(name = "impact")
	private String impact;

	@Column(name = "impact_id")
	private String impactId;

	@Column(name = "urgency")
	private String urgency;

	@Column(name = "urgency_id")
	private String urgencyId;

	@Column(name = "priority")
	private String priority;

	@Column(name = "priority_id")
	private String priorityId;

	@Column(name = "source")
	private String source;
	
	@Column(name = "full_name")
	private String fullName;

	

	@Column(name = "source_contact")
	private String sourceContact;

	@Column(name = "group_name")
	private String assignmentGroup;

	@Column(name = "assign_to")
	private String assignTo;

	@Column(name = "assign_to_id")
	private String assignToId;

	@Column(name = "comments")
	private String comments;

	@Column(name = "module")
	private String module;

	@Column(name = "resolution_method")
	private String resolutionMethod;

	@Column(name = "resolution_method_id")
	private String resolutionMethodId;

	@Column(name = "resolution_type")
	private String resolutionType;

	
	@Column(name = "resolution_remarks")
	private String resolutionRemarks;

	@Column(name = "descriptions")
	private String descriptions;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getPublishForGroups() {
		return publishForGroups;
	}

	public void setPublishForGroups(String publishForGroups) {
		this.publishForGroups = publishForGroups;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getResolutionMethod() {
		return resolutionMethod;
	}

	public void setResolutionMethod(String resolutionMethod) {
		this.resolutionMethod = resolutionMethod;
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

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getImpactId() {
		return impactId;
	}

	public void setImpactId(String impactId) {
		this.impactId = impactId;
	}

	public String getUrgencyId() {
		return urgencyId;
	}

	public void setUrgencyId(String urgencyId) {
		this.urgencyId = urgencyId;
	}

	public String getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(String priorityId) {
		this.priorityId = priorityId;
	}

	
	public String getAssignToId() {
		return assignToId;
	}

	public void setAssignToId(String assignToId) {
		this.assignToId = assignToId;
	}

	public String getResolutionMethodId() {
		return resolutionMethodId;
	}

	public void setResolutionMethodId(String resolutionMethodId) {
		this.resolutionMethodId = resolutionMethodId;
	}

	

	public String getTamplateName() {
		return tamplateName;
	}

	public void setTamplateName(String tamplateName) {
		this.tamplateName = tamplateName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "TemplateEntity [templateId=" + templateId + ", tamplateName=" + tamplateName + ", active=" + active
				+ ", publishForGroups=" + publishForGroups + ", category=" + category + ", categoryId=" + categoryId
				+ ", subCategory=" + subCategory + ", subCategoryId=" + subCategoryId + ", title=" + title + ", impact="
				+ impact + ", impactId=" + impactId + ", urgency=" + urgency + ", urgencyId=" + urgencyId
				+ ", priority=" + priority + ", priorityId=" + priorityId + ", source=" + source + ", fullName="
				+ fullName + ", sourceContact=" + sourceContact + ", assignmentGroup=" + assignmentGroup + ", assignTo="
				+ assignTo + ", assignToId=" + assignToId + ", comments=" + comments + ", module=" + module
				+ ", resolutionMethod=" + resolutionMethod + ", resolutionMethodId=" + resolutionMethodId
				+ ", resolutionType=" + resolutionType + ", resolutionRemarks=" + resolutionRemarks + ", descriptions="
				+ descriptions + "]";
	}

	

}
