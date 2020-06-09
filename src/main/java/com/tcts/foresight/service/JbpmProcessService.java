package com.tcts.foresight.service;

public interface JbpmProcessService {

	String createProcessInstance(String authToken, String containerId, String processId, String clientCode,
			String requestPayload);

	String getTaskId(String authToken, String processInstanceId);

	String getOnlyTaskId(String authToken, String processInstanceId);

}
