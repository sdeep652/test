package com.tcts.foresight.entity.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "known_error_details")
public class KnownErrorEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "known_error_details_seq_generator")
	@SequenceGenerator(name = "known_error_details_seq_generator", sequenceName = "known_error_details_seq", allocationSize = 1)
	@Column(name = "id")
	private Long Id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;
	
	@Column(name = "workaround")
	private String workaround;
	
	@JsonIgnore
	@Column(name = "problem_id")
	private String problemID;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorkaround() {
		return workaround;
	}

	public void setWorkaround(String workaround) {
		this.workaround = workaround;
	}

	public String getProblemID() {
		return problemID;
	}

	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}

	@Override
	public String toString() {
		return "KnownErrorEntity [Id=" + Id + ", title=" + title + ", description=" + description + ", workaround="
				+ workaround + ", problemID=" + problemID + "]";
	}
	
	



}
