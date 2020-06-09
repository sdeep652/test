package com.tcts.foresight.entity;

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
@Table(name = "problem_notification_details")
public class ProblemNotificationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_notification_details_seq_generator")
	@SequenceGenerator(name="problem_notification_details_seq_generator", sequenceName = "problem_notification_details_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	/*
	 * @Type(type = "com.tcts.foresight.jbpm.db.GenericArrayUserType")
	 * 
	 * @Column(name = "incident_id") private String[] incidentIDArray;
	 */

	
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "notification_id", referencedColumnName = "id", nullable = false)
	public List<ProblemNotificationIncident> incidentIDList;
 


	

	@Column(name = "is_enable")
	private String isEnable;	

	@Column(name = "notification")
	private String notification;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public List<ProblemNotificationIncident> getIncidentIDList() {
		return incidentIDList;
	}

	public void setIncidentIDList(List<ProblemNotificationIncident> incidentIDList) {
		this.incidentIDList = incidentIDList;
	}

	@Override
	public String toString() {
		return "ProblemNotificationEntity [id=" + id + ", incidentIDList=" + incidentIDList + ", isEnable=" + isEnable
				+ ", notification=" + notification + "]";
	}

	


	
	
}
