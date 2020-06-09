package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "resolution_type_details")
public class ResolutionTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resolution_type_seq_generator")
	@SequenceGenerator(name="resolution_type_seq_generator", sequenceName = "resolution_type_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	public String getResolutiontype() {
		return resolutiontype;
	}

	public void setResolutiontype(String resolutiontype) {
		this.resolutiontype = resolutiontype;
	}

	@Column(name = "resolutiontype")
	private String resolutiontype;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "module")
	private String module;

	@Override
	public String toString() {
		return "ResolutionTypeEntity [id=" + id + ", resolutiontype=" + resolutiontype + ", module=" + module + "]";
	}

}
