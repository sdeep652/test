package com.tcts.foresight.entity.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "problem_status_details")
public class ProblemStatusDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_status_seq_generator")
	@SequenceGenerator(name = "problem_status_seq_generator", sequenceName = "problem_status_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "status")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
