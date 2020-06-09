package com.tcts.foresight.jbpm.db.pojo;



public class ActualVariableInstanceLogVO {

	private String processInstanceId;
	private String processId;
	private String value;
	private String variableid;
	
	public ActualVariableInstanceLogVO() {
		
	}
	
	
	public ActualVariableInstanceLogVO(String processInstanceId, String processId, String value, String variableid) {
		super();
		this.processInstanceId = processInstanceId;
		this.processId = processId;
		this.value = value;
		this.variableid = variableid;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getVariableid() {
		return variableid;
	}
	public void setVariableid(String variableid) {
		this.variableid = variableid;
	}


	@Override
	public String toString() {
		return "ActualVariableInstanceLogVO [processInstanceId=" + processInstanceId + ", processId=" + processId
				+ ", value=" + value + ", variableid=" + variableid + "]";
	}
	
	
	
}

