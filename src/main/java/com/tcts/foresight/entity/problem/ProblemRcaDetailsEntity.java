package com.tcts.foresight.entity.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "problem_rca_details")
public class ProblemRcaDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_rca_seq_generator")
	@SequenceGenerator(name = "problem_rca_seq_generator", sequenceName = "problem_rca_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "rca")
	private String rca;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRca() {
		return rca;
	}

	public void setRca(String rca) {
		this.rca = rca;
	}

}
