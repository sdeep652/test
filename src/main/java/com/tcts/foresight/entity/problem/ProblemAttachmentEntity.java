package com.tcts.foresight.entity.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "problem_details_attachments")
public class ProblemAttachmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_attachment_seq_generator")
	@SequenceGenerator(name = "problem_attachment_seq_generator", sequenceName = "problem_attachment_seq", allocationSize = 1)
	@Column(name = "id")
	private Long Id;

	@Column(name = "problem_ticket_id")
	private String problemID;

	@Column(name = "attachment_name")
	private String attachmentName;

	@Transient
	private String attachment;
	
	@JsonIgnore
	@Column(name = "attachment_details")
	private byte[] byteAttachment;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getProblemID() {
		return problemID;
	}

	public void setProblemID(String problemID) {
		this.problemID = problemID;
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

	public byte[] getByteAttachment() {
		return byteAttachment;
	}

	public void setByteAttachment(byte[] byteAttachment) {
		this.byteAttachment = byteAttachment;
	}

}
