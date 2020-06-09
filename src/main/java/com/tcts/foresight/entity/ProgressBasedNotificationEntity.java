package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "progresbased_notification")
public class ProgressBasedNotificationEntity {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "progresbased_notification_seq_generator")
	@SequenceGenerator(name = "progresbased_notification_seq_generator", sequenceName = "progresbased_notification_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "email_list")
	private String emailList;

	@Column(name = "phone_number_list")
	private String phoneNumberList;
	

	@Column(name = "module")
	private String module;

	

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



	public String getModule() {
		return module;
	}



	public void setModule(String module) {
		this.module = module;
	}



	@Override
	public String toString() {
		return "ProgressBasedNotificationEntity [id=" + id + ", emailList=" + emailList + ", phoneNumberList="
				+ phoneNumberList + ", module=" + module + "]";
	}

	
    
}
