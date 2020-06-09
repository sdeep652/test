package com.tcts.foresight.pojo;

public class PriorityDetails {
	
	private Long id;
	private String name;
	private Long impactId;
	private Long urgencyId;
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
	public Long getImpactId() {
		return impactId;
	}
	public void setImpactId(Long impactId) {
		this.impactId = impactId;
	}
	public Long getUrgencyId() {
		return urgencyId;
	}
	public void setUrgencyId(Long urgencyId) {
		this.urgencyId = urgencyId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	@Override
	public String toString() {
		return "PriorityDetails [id=" + id + ", name=" + name + ", impactId=" + impactId + ", urgencyId=" + urgencyId
				+ ", module=" + module + "]";
	}
	

}
