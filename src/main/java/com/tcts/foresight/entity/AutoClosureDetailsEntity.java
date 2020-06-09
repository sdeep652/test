package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "autoclosure_details")
public class AutoClosureDetailsEntity extends AuthMessageEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autoclosure_seq_generator")
	@SequenceGenerator(name = "autoclosure_seq_generator", sequenceName = "autoclosure_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cat_id")
	private CategoryDetailsEntity catDetails;
	
	@ManyToOne
	@JoinColumn(name = "priority_id")
	private PriorityDetailsEntity priorityDetails;
	
	@Column(name="time_in_mins")
	private String autoCloseTime;
	
	@Column(name="module")
	private String module;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategoryDetailsEntity getCatDetails() {
		return catDetails;
	}

	public void setCatDetails(CategoryDetailsEntity catDetails) {
		this.catDetails = catDetails;
	}

	public PriorityDetailsEntity getPriorityDetails() {
		return priorityDetails;
	}

	public void setPriorityDetails(PriorityDetailsEntity priorityDetails) {
		this.priorityDetails = priorityDetails;
	}

	public String getAutoCloseTime() {
		return autoCloseTime;
	}

	public void setAutoCloseTime(String autoCloseTime) {
		this.autoCloseTime = autoCloseTime;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "AutoClosureDetailsEntity [id=" + id + ", catDetails=" + catDetails + ", priorityDetails="
				+ priorityDetails + ", autoCloseTime=" + autoCloseTime + ", module=" + module + "]";
	}
	
}
