package com.tcts.foresight.scheduler.entity;

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
@Table(name="sla_configuration")
public class SLAConfigurationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sla_config_seq_generator")
	@SequenceGenerator(name = "sla_config_seq_generator", sequenceName = "sla_config_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "sla_name")
	private String slaName;
	
	@Column(name = "sla_type")
	private String slaType;
	
	@Column(name = "sla_target")
	private String slaTarget;
	
	@Column(name = "is_active")
	private String isActive;
	
	@Column(name = "module")
	private String module;
	
	@Column(name = "time_in_days")
	private String timeInDays;
	
	@Column(name = "time_in_hours")
	private String timeInHours;
	
	@Column(name = "time_in_mins")
	private String timeInMins;
	
	@Column(name = "time_in_secs")
	private String timeInSecs;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "sla_config_id", referencedColumnName = "id", nullable = false)
    private List<SLAConfigurationActionsEntity> slaConfigActionsEList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getSlaTarget() {
		return slaTarget;
	}

	public void setSlaTarget(String slaTarget) {
		this.slaTarget = slaTarget;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<SLAConfigurationActionsEntity> getSlaConfigActionsEList() {
		
		return slaConfigActionsEList;
	}

	public void setSlaConfigActionsEList(List<SLAConfigurationActionsEntity> slaConfigActionsEList) {
		this.slaConfigActionsEList = slaConfigActionsEList;
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

	@Override
	public String toString() {
		return "SLAConfigurationEntity [id=" + id + ", slaName=" + slaName + ", slaType=" + slaType + ", slaTarget="
				+ slaTarget + ", isActive=" + isActive + ", module=" + module + ", timeInDays=" + timeInDays
				+ ", timeInHours=" + timeInHours + ", timeInMins=" + timeInMins + ", timeInSecs=" + timeInSecs
				+ ", slaConfigActionsEList=" + slaConfigActionsEList + "]";
	}
	
}
