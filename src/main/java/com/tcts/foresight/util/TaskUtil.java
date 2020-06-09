package com.tcts.foresight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcts.foresight.client.JBPMClientConfig;

@Component
public class TaskUtil {

	Logger logger = LoggerFactory.getLogger(TaskUtil.class);

	@Autowired
	private JBPMClientConfig jBPMClientConfig;
	private static String skipTaskUserName = "ttadamin";
	private static String skipTaskPaswd = "ttadmin";
	static {

		if (System.getenv("SKIP_TASK_USER_NAME") != null)
			skipTaskUserName = System.getenv("SKIP_TASK_USER_NAME");
		if (System.getenv("SKIP_TASK_PASSWORD") != null)
			skipTaskPaswd = System.getenv("SKIP_TASK_PASSWORD");
	}

	public static ArrayList<JSONObject> getFilteredJobListData(HashMap<String, String> filter,
			ArrayList<JSONObject> completeJobList) {
		ArrayList<JSONObject> filteredList = new ArrayList<JSONObject>();

		boolean orConditionMatched = false;
		for (JSONObject jsonObject : completeJobList) {
			orConditionMatched = false;

			boolean recordFound = true;
			for (String currentKey : filter.keySet()) {
				orConditionMatched = handleOrConditionInFilterJSONMap(filter, jsonObject, currentKey);
				if (orConditionMatched) {
					continue;
				}

				else if (filter.get(currentKey).contains("!")) {
					String finalValue = filter.get(currentKey).substring(1).trim();
					if (jsonObject != null && jsonObject.has(currentKey)
							&& !(jsonObject.get(currentKey).equals(finalValue))) {
						recordFound = true;
					} else {
						recordFound = false;
					}

				} else {
					recordFound = false;
				}
			}
			if (recordFound) {

				filteredList.add(jsonObject);
			}
		}
		return filteredList;
	}

	public ArrayList<JSONObject> getCompleteJobListData(String authToken, String containerId) {
		ArrayList<JSONObject> completeJobList = new ArrayList<JSONObject>();
		String responseString = null;

		String queryId = Constant.VM_55_AllData_QueryId;
		String path = Constant.QURES_DEFINITIONS + queryId + Constant.FILTER_DATA;

		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(Constant.MAPPER, "ProcessInstancesWithVariables");
		queryParams.put("page", "0");
		queryParams.put(Constant.PAGE_SIZE, "-1");
		Response response = jBPMClientConfig.postRequest(authToken, path, queryParams, "");
		JSONObject mainObject = null;

		if (response != null) {

			responseString = response.readEntity(String.class);

		}

		JSONObject jsonResponse = new JSONObject(responseString);
		JSONObject processData = new JSONObject();

		JSONArray mainArray = (JSONArray) jsonResponse.get("process-instance");
		JSONObject processDataNew = new JSONObject();

		for (int i = 0; i < mainArray.length(); i++) {
			mainObject = null;
			processData = mainArray.getJSONObject(i);

			Iterator<String> keys = processData.keys();
			String key = null;

			if (keys != null) {
				while (keys.hasNext()) {
					key = keys.next();
					if (StringUtil.isNotNullNotEmpty(key)) {
						if (!key.equalsIgnoreCase("process-instance-variables")) {
							processDataNew.put(key, processData.get(key).toString());
						}
					}
				}
			}

			String obj = processData.get("process-instance-variables").toString();
			JSONObject internalTaskObject = new JSONObject(obj);

			try {

				if (internalTaskObject != null
						&& StringUtil.isNotNullNotEmpty((String) internalTaskObject.get("ticketNo"))) {

					mainObject = JSONUtil.mergeJSONObjects(processDataNew, internalTaskObject);
					completeJobList.add(mainObject);
				}
			} catch (Exception e) {
				logger.error("Exception occured in getCompleteJobListData: " + e.getMessage(), e);  

			}

		}

		return completeJobList;
	}

	private static boolean handleOrConditionInFilterJSONMap(HashMap<String, String> filter, JSONObject jsonObject,
			String currentKey) {

		ArrayList<String> orConditions = null;
		boolean orConditionMatched = false;

		if (StringUtil.isNotNullNotEmpty(currentKey) && StringUtil.isNotNullNotEmpty(filter.get(currentKey))) {
			String value = filter.get(currentKey);

			if (value.contains(",")) {
				StringTokenizer stn = new StringTokenizer(value, ",");
				orConditions = new ArrayList<String>();

				if (stn != null) {
					while (stn.hasMoreTokens()) {
						String orConditionString = stn.nextToken();
						if (StringUtil.isNotNullNotEmpty(orConditionString)) {
							orConditions.add(orConditionString.trim());
						}
					}
				}

				if (orConditions != null && orConditions.size() > 0) {
					for (String orCondition : orConditions) {
						if (jsonObject != null && jsonObject.has(currentKey)
								&& StringUtil.isNotNullNotEmpty((String) jsonObject.get(currentKey))
								&& jsonObject.get(currentKey).equals(orCondition)) {
							orConditionMatched = true;
							break;
						}
					}

				}

			}

		}
		return orConditionMatched;
	}

	public ArrayList<JSONObject> getJobDataByProcessId(String authToken, String containerId, String body) {
		ArrayList<JSONObject> completeJobList = new ArrayList<JSONObject>();
		String responseString = null;

		String queryId = Constant.VM_55_AllData_QueryId;
		String path = Constant.QURES_DEFINITIONS + queryId + Constant.FILTER_DATA;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(Constant.MAPPER, "ProcessInstancesWithVariables");
		queryParams.put("page", "0");
		queryParams.put(Constant.PAGE_SIZE, "-1");
		Response response = jBPMClientConfig.postRequest(authToken, path, queryParams, body);
		JSONObject mainObject = null;
		if (response != null) {
			responseString = response.readEntity(String.class);

		}

		JSONObject jsonResponse = new JSONObject(responseString);
		JSONObject processData = null;

		JSONArray mainArray = (JSONArray) jsonResponse.get("process-instance");
		JSONObject processDataNew = new JSONObject();

		for (int i = 0; i < mainArray.length(); i++) {
			processData = mainArray.getJSONObject(i);

			Iterator<String> keys = processData.keys();
			String key = null;

			if (keys != null) {
				while (keys.hasNext()) {
					key = keys.next();
					if (StringUtil.isNotNullNotEmpty(key)) {
						if (!key.equalsIgnoreCase("process-instance-variables")) {
							processDataNew.put(key, processData.get(key).toString());
						}
					}
				}
			}

			String obj = processData.get("process-instance-variables").toString();
			JSONObject internalTaskObject = new JSONObject(obj);

			try {

				if (internalTaskObject != null
						&& StringUtil.isNotNullNotEmpty((String) internalTaskObject.get("ticketNo"))) {

					mainObject = JSONUtil.mergeJSONObjects(processDataNew, internalTaskObject);
					completeJobList.add(mainObject);
				}
			} catch (Exception e) {
				logger.error("** ERROR while get job data by process id -- " + e.getMessage(),e);
			}

		}
		return completeJobList;
	}

}
