package com.tcts.foresight.pojo;

public class RoleDetailsDTO {
	
	private String id;
	private String roleName;
//	private String module;
	
	public RoleDetailsDTO() {}
	public RoleDetailsDTO(String id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
//		this.module = module;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
//	public String getModule() {
//		return module;
//	}
//	public void setModule(String module) {
//		this.module = module;
//	}
	
}
