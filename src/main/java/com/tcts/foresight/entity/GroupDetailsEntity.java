package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "group_details")
public class GroupDetailsEntity extends AuthMessageEntity {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String groupName;

	@Column(name = "email")
	private String email;

	@Column(name = "is_read_only_group")
	private String isReadOnlyGroup;

	@Column(name = "default_group")
	private String defaultGroup = "NO";

	@Column(name = "default_module_group")
	private String defaultModuleGroup ="NO";
	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "group_id") private List<GroupRoleEntity> grpRoleList;
	 */

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "group_role", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id", unique = false) })
	private List<RoleDetailsEntity> grpRoleList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<RoleDetailsEntity> getGrpRoleList() {
		return grpRoleList;
	}

	public void setGrpRoleList(List<RoleDetailsEntity> grpRoleList) {
		this.grpRoleList = grpRoleList;
	}

	public String getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public String getIsReadOnlyGroup() {
		return isReadOnlyGroup;
	}

	public void setIsReadOnlyGroup(String isReadOnlyGroup) {
		this.isReadOnlyGroup = isReadOnlyGroup;
	}

	public String getDefaultModuleGroup() {
		return defaultModuleGroup;
	}

	public void setDefaultModuleGroup(String defaultModuleGroup) {
		this.defaultModuleGroup = defaultModuleGroup;
	}

}
