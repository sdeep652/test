package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "resolution_method_details")
public class ResolutionMethodEntity extends AuthMessageEntity {

	 @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resolution_method_seq_generator")
	@SequenceGenerator(name="resolution_method_seq_generator", sequenceName = "resolution_method_seq", allocationSize = 1)
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
		return "ResolutionMetEntity [id=" + id + ", name=" + name + ", module=" + module + "]";
	}

}
