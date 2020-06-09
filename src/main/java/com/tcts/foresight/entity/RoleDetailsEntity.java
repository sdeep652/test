package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcts.foresight.pojo.RoleDetailsDTO;

@Entity
@Table(name = "role_details")
public class RoleDetailsEntity extends AuthMessageEntity{

	 @Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String roleName;

//	@Column(name = "module")
//	private String module;
	
	@Column(name = "is_read_only_role")
	private String isReadOnlyRole;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "role_id") private List<RolePageEntity> pageAccessControl;
	 */

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "role_page_access", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "page_id", unique = false) })
	private List<PageNamesDetailsEntity> pageAccessControl;

	public RoleDetailsEntity() {
	}
	
	public RoleDetailsEntity(String id, String roleName, String module) {
		this.id = id;
		this.roleName = roleName;
	//	this.module = module;
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
//
//	public void setModule(String module) {
//		this.module = module;
//	}

	public List<PageNamesDetailsEntity> getPageAccessControl() {
		return pageAccessControl;
	}

	public void setPageAccessControl(List<PageNamesDetailsEntity> pageAccessControl) {
		this.pageAccessControl = pageAccessControl;
	}
	
	public String getIsReadOnlyRole() {
		return isReadOnlyRole;
	}

	public void setIsReadOnlyRole(String isReadOnlyRole) {
		this.isReadOnlyRole = isReadOnlyRole;
	}
	
	

	@JsonIgnore
	public RoleDetailsDTO getRoleDetailsDTO() {
		RoleDetailsDTO roleDetailsDTO = new RoleDetailsDTO();
		roleDetailsDTO.setId(this.getId());
		roleDetailsDTO.setRoleName(this.getRoleName());
//		roleDetailsDTO.setModule(this.getModule());
		return roleDetailsDTO;
	}

	

}
