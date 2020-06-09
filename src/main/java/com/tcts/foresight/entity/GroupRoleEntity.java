package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcts.foresight.pojo.RoleDetailsDTO;

@Entity
@Table(name="group_role")
public class GroupRoleEntity {
	

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_role_seq_generator")
	@SequenceGenerator(name="group_role_seq_generator", sequenceName = "group_role_seq", allocationSize = 1)
	@Column(name = "role_id")
	private String id;
	
	
	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "module")
	private String module;

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
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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
