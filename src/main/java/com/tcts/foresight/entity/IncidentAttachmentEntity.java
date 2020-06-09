package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "incident_attachments")
public class IncidentAttachmentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq_generator")
	@SequenceGenerator(name = "attachment_seq_generator", sequenceName = "attachment_seq", allocationSize = 1)
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "ticket_id")
	private String incidentID;
	
	@Column(name = "attachment_name")
	private String attachmentName;
	
	@Column(name = "attachment_details")
	private String attachment;

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

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
}
