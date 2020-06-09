package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "impact_details")
public class ImpactDetailsEntity {


    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impact_details_seq_generator")
	@SequenceGenerator(name="impact_details_seq_generator", sequenceName = "impact_details_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;
	@Column(name = "module")
	private String module;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	@Override
	public String toString() {
		return "ImpactDetailsEntity [id=" + id + ", name=" + name + ", module=" + module + "]";
	}

	


}
