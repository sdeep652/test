package com.tcts.foresight.pojo;

public class CategoryDetails {

	private Long id;

	private String name;
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
		return "CategoryDetails [id=" + id + ", name=" + name + ", module=" + module + "]";
	}

	
	

}
