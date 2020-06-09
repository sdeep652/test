package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "category_details")
public class CategoryDetailsEntity extends AuthMessageEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_generator")
	@SequenceGenerator(name="category_seq_generator", sequenceName = "category_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	

	@Column(name = "name")
	private String name;

	@Column(name = "module")
	private String module;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

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

	@Override
	public String toString() {
		return "CategoryDetailsEntity [id=" + id + ", name=" + name + ", module=" + module + "]";
	}

}
