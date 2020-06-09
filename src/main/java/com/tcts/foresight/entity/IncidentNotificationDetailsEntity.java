package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "incident_notifications_detail")
public class IncidentNotificationDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "id")
	private Long Id;

	@Column(name = "ticket_id")
	private String ticketid;
	
	@Column(name = "workflow_Id")
	private Long workflowId;

	@Column(name = "notification_sent_till")
	private Long notificationSentTill;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	
	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Long getNotificationSentTill() {
		return notificationSentTill;
	}

	public void setNotificationSentTill(Long notificationSentTill) {
		this.notificationSentTill = notificationSentTill;
	}

	

	public String getTicketid() {
		return ticketid;
	}

	public void setTicketid(String ticketid) {
		this.ticketid = ticketid;
	}

	@Override
	public String toString() {
		return "IncidentNotificationDetailsEntity [Id=" + Id + ", ticketid=" + ticketid + ", workflowId=" + workflowId
				+ ", notificationSentTill=" + notificationSentTill + "]";
	}

	
	
	
	
	


}
