package com.tcts.foresight.scheduler.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sla_config_workflow")
public class SLAConfigWorkFlow implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "sla_config_id")
	private String slaConfigId;

	@Column(name = "sla_elapsed")
	private Long slaElapsed;

	@Column(name = "email_list")
	private String emailList;

	@Column(name = "phone_number_list")
	private String phoneNumberList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailList() {
		return emailList;
	}

	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}

	public String getPhoneNumberList() {
		return phoneNumberList;
	}

	public void setPhoneNumberList(String phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}

	public String getSlaConfigId() {
		return slaConfigId;
	}

	public void setSlaConfigId(String slaConfigId) {
		this.slaConfigId = slaConfigId;
	}

	public Long getSlaElapsed() {
		return slaElapsed;
	}

	public void setSlaElapsed(Long slaElapsed) {
		this.slaElapsed = slaElapsed;
	}

}
