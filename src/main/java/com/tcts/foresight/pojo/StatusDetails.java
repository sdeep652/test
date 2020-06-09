package com.tcts.foresight.pojo;

public class StatusDetails {
	
	private Long id;
	private String status;
	private Long remark;
	private String module;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getRemark() {
		return remark;
	}
	public void setRemark(Long remark) {
		this.remark = remark;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	@Override
	public String toString() {
		return "StatusDetails [id=" + id + ", status=" + status + ", remark=" + remark + ", module=" + module + "]";
	}

}
