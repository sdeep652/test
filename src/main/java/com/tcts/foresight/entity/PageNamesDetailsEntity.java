package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pagenames_details")
public class PageNamesDetailsEntity {
	

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagenames_seq_generator")
	@SequenceGenerator(name="pagenames_seq_generator", sequenceName = "pagenames_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "pagename")
	private String pageName;
	
	@Column(name = "module")
	private String module;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
}
