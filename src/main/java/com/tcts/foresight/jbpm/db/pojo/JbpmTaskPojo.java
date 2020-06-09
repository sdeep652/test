package com.tcts.foresight.jbpm.db.pojo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = JbpmTaskPojo.class)
public class JbpmTaskPojo {

	
	private String containerid;
	private String processid;
	private String client;
	private String processInstanceId;
	
	public JbpmTaskPojo() {
		
	}
	
	
	public String getContainerid() {
		return containerid;
	}
	
	
	public void setContainerid(String containerid) {
		this.containerid = containerid;
	}
	public String getProcessid() {
		return processid;
	}
	public JbpmTaskPojo(String containerid, String processid, String client, String processInstanceId) {
		super();
		this.containerid = containerid;
		this.processid = processid;
		this.client = client;
		this.processInstanceId = processInstanceId;
	}


	public void setProcessid(String processid) {
		this.processid = processid;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
	
}

