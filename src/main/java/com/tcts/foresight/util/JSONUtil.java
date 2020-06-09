package com.tcts.foresight.util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

	public static HashMap<String, Object[]> taskJsonToHashMap(String jsonString) {
		HashMap<String, Object[]> map = new HashMap<String, Object[]>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			// convert JSON string to Map
			map = mapper.readValue(jsonString, new TypeReference<HashMap<String, Object[]>>() {
			});

		} catch (Exception e) {
			logger.error("Exception occured in taskJsonToHashMap: " + e.getMessage(), e);  
		}
		return map;
	}

	public HashMap<String, Object> objectToHashMap(Object object) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (object != null)
				map = mapper.convertValue(object, HashMap.class);
		} catch (Exception e) {
			logger.error("Exception occur while converting object to hashmap---" + e.getMessage(),e);
		}
		return map;
	}

	public HashMap<String, Object> jsonToHashMap(String jsonString) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			// convert JSON string to Map
			map = mapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
			});

		} catch (Exception e) {
			logger.error("Exception occur while converting json to hashmap---" + e.getMessage(),e);
		}
		return map;
	}

	public static HashMap<String, String> jsonpayloadMapToHashMap(String jsonString) {
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		if (StringUtil.isNotNullNotEmpty(jsonString)) {
			JSONObject filteredStatus = new JSONObject(jsonString);
			Iterator<String> keys = filteredStatus.keys();

			String key = null;
			if (keys != null) {
				while (keys.hasNext()) {
					key = keys.next();
					if (StringUtil.isNotNullNotEmpty(key)) {
						jsonMap.put(key, filteredStatus.get(key).toString());
					}
				}
			}

		} else {
		}
		return jsonMap;
	}

	public static String objectToJson(Object object) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			// convert Object to string
			json = mapper.writeValueAsString(object);
			//logger.info(json);
		} catch (Exception e) {
			logger.error("Exception occur while converting object to string---" + e.getMessage(),e);
		}
		return json;
	}

	public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
		JSONObject mergedJSON = new JSONObject();
		try {
			mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
			for (String crunchifyKey : JSONObject.getNames(json2)) {
				mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
			}

		} catch (JSONException e) {
			logger.error("Exception occur while mergeJSONObjects---" + e.getMessage(),e);
		}
		return mergedJSON;
	}

}
