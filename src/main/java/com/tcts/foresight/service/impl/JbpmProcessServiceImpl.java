package com.tcts.foresight.service.impl;

import java.util.HashMap;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.client.JBPMClientConfig;
import com.tcts.foresight.service.JbpmProcessService;
import com.tcts.foresight.util.StringUtil;

@Service
public class JbpmProcessServiceImpl implements JbpmProcessService {
	Logger logger = LoggerFactory.getLogger(JbpmProcessServiceImpl.class);

	@Autowired
	private JBPMClientConfig jbpmClient;

	@Override
	public String createProcessInstance(String authToken, String containerId, String processId, String clientCode,
			String requestPayload) {
		String processInstanceId = "";
		try {
			String path = "containers/" + containerId + "/processes/" + processId + "/instances";
			Response response = jbpmClient.postRequest(authToken, path, requestPayload);
			processInstanceId = response.readEntity(String.class);
			//logger.info("Process Instance Id:- " + processInstanceId);
		} catch (Exception e) {
			logger.error("Exception occur while in createProcessInstance"+e.getMessage(),e);
			throw e;
		}
		
		return processInstanceId;
	}
	

	@Override
	public String getTaskId(String authToken, String processInstanceId) {
		String jsonData = null;

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("page", "0");
		queryParams.put("pageSize", "10");
		queryParams.put("sortOrder", "true");
		String path = "queries/tasks/instances/process/" + processInstanceId;
		jsonData = jbpmClient.getRequest(authToken, path, queryParams).readEntity(String.class);
		//logger.info("Task Details: "+ jsonData);

		return jsonData;
	}
	
	@Override
	public String getOnlyTaskId(String authToken, String processInstanceId) {
		String jsonData = null;
		String taskInstanceIdNew = null;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("page", "0");
		queryParams.put("pageSize", "10");
		queryParams.put("sortOrder", "true");
		String path = "queries/tasks/instances/process/" + processInstanceId;
		jsonData = jbpmClient.getRequest(authToken, path, queryParams).readEntity(String.class);
		//logger.info("Task Details: "+ jsonData);

		if(StringUtil.isNotNullNotEmpty(jsonData))
		{
			JSONObject jobj = new JSONObject(jsonData);
			JSONArray jArray = jobj.getJSONArray("task-summary");
			taskInstanceIdNew = jArray.getJSONObject(0).optString("task-id");
			
		}
		
		return taskInstanceIdNew;
	}

}
