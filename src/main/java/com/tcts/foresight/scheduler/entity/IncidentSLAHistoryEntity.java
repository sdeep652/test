package com.tcts.foresight.scheduler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "incident_sla_history")
public class IncidentSLAHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incident_sla_his_seq_generator")
	@SequenceGenerator(name = "incident_sla_his_seq_generator", sequenceName = "incident_sla_his_seq", allocationSize = 1)
	@Column(name = "id")
	private Long Id;

	@Column(name = "ticket_id")
	private String incidentID;

	@Column(name = "sla_type")
	private String slaType;
	
	@Column(name="notifications_sent_till")
	private Long notificationsSentTill;

	@Column(name = "sla_config_id")
	private Long slaConfigId;
	
	@Column(name = "sla_name")
	private String slaName;

	
	@Column(name = "sla_time_remaining")
	private String slaTimeRemaining;
	
	
	@Column(name = "sla_status")
	private String slaStatus;
	
	@Column(name = "sla_target_time")
	private String slaTargetTime;

	
	@Column(name = "created_time")
	private String createdDate;

	@Column(name = "last_updated_date")
	private String lastUpdatedDate;

	@Column(name = "sla_completion_date")
	private String slaCompletionDate;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getSlaStatus() {
		return slaStatus;
	}

	public void setSlaStatus(String slaStatus) {
		this.slaStatus = slaStatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getSlaCompletionDate() {
		return slaCompletionDate;
	}

	public void setSlaCompletionDate(String slaCompletionDate) {
		this.slaCompletionDate = slaCompletionDate;
	}

	public Long getSlaConfigId() {
		return slaConfigId;
	}

	public void setSlaConfigId(Long slaConfigId) {
		this.slaConfigId = slaConfigId;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public String getSlaTimeRemaining() {
		return slaTimeRemaining;
	}

	public void setSlaTimeRemaining(String slaTimeRemaining) {
		this.slaTimeRemaining = slaTimeRemaining;
	}

	public String getSlaTargetTime() {
		return slaTargetTime;
	}

	public void setSlaTargetTime(String slaTargetTime) {
		this.slaTargetTime = slaTargetTime;
	}

	@Override
	public String toString() {
		return "IncidentSLAHistoryEntity [Id=" + Id + ", incidentID=" + incidentID + ", slaType=" + slaType
				+ ", slaConfigId=" + slaConfigId + ", slaName=" + slaName + ", slaTimeRemaining=" + slaTimeRemaining
				+ ", slaStatus=" + slaStatus + ", slaTargetTime=" + slaTargetTime + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", slaCompletionDate=" + slaCompletionDate + "]";
	}

	public Long getNotificationsSentTill() {
		return notificationsSentTill;
	}

	public void setNotificationsSentTill(Long notificationsSentTill) {
		this.notificationsSentTill = notificationsSentTill;
	}
	
	

}
