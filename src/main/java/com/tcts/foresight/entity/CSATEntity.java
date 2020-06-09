package com.tcts.foresight.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_sat")
public class CSATEntity implements Serializable{

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "rating")
	private Long rating;

	@Column(name = "reason")
	private String reason;
	
	
	@Column(name = "question")
	private String question;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

}
