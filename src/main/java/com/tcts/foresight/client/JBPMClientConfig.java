package com.tcts.foresight.client;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class JBPMClientConfig {

	Logger logger = LoggerFactory.getLogger(JBPMClientConfig.class);

	@Value("${bpm.server.host}")
	private String jHostName;

	@Value("${bpm.server.port}")
	private String jPort;

	
	@Value("${jbpm.auth}")
	private String jbpmAuth;

	@Value("${jbpm.auth.type}")
	private String jbpmAuthType;

	@Value("${jbpm.status}")
	private String jbpmStatus;

	public static WebTarget webTarget = null;

	public void getClientBuilder() {
		StringBuilder builder = null;
		if (jHostName != null && jPort != null) {
			builder = new StringBuilder();
			builder.append("http://");
			builder.append(jHostName);
			builder.append(":");
			builder.append(jPort);
			builder.append("/kie-server/services/rest/server/");

			Client client = ClientBuilder.newClient();

			client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
			client.property(ClientProperties.READ_TIMEOUT, 20000);

			JBPMClientConfig.webTarget = client.target(builder.toString());

		} else {
			
		}

	
	}

	public String getCookieValue() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String cookieValue = null;
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				//logger.info("Key: " + key + ", Value: " + value);
				if (key.equalsIgnoreCase("cookie")) {
					//logger.info("Key: " + key + ", Value: " + value);
					cookieValue = value;
					break;
				}
			}

		}
		return cookieValue;
	}

	public Response getRequest(String authToken, String path) {
		Response response = null;
		getClientBuilder();
		if (path != null && authToken != null) {
			Invocation.Builder invocationBuilder = webTarget.path(path).request(MediaType.APPLICATION_JSON)
					.header(jbpmAuth, jbpmAuthType + authToken).header("Content-Type", "application/json");
			response = invocationBuilder.get();
			//logger.info("===============================Get Response=====================" + response.getStatus());
		}
		return response;
	}

	public Response postRequest(String authToken, String path, String requestPayload) {
		Response response = null;
		getClientBuilder();
		String cookieValue = getCookieValue();
		if (jbpmAuth != null && jbpmAuthType != null) {
			Invocation.Builder invocationBuilder = webTarget.path(path).request(MediaType.APPLICATION_JSON)
					.header(jbpmAuth, jbpmAuthType + authToken);
			//logger.info("***************cookieValue**************" + cookieValue);
			response = invocationBuilder.cookie("Cookie", cookieValue)
					.post(Entity.entity(requestPayload, MediaType.APPLICATION_JSON));
		}

		//logger.info("===============================POST Response=====================" + response.getStatus());
		return response;
	}

	public Response putRequest(String authToken, String path, String requestPayload) {
		Response response = null;
		getClientBuilder();
		String cookieValue = getCookieValue();
		if (jbpmAuth != null && jbpmAuthType != null) {
			Invocation.Builder invocationBuilder = webTarget.path(path).request(MediaType.APPLICATION_JSON)
					.header(jbpmAuth, jbpmAuthType + authToken);
			//logger.info("***************cookieValue**************" + cookieValue);
			response = invocationBuilder.cookie("Cookie", cookieValue)
					.put(Entity.entity(requestPayload, MediaType.APPLICATION_JSON));
		}
		//logger.info("===============================PUT Response=====================" + response.getStatus());
		return response;
	}

	public Response deleteRequest(String authToken, String path) {
		Response response = null;
		getClientBuilder();
		if (jbpmAuth != null && jbpmAuthType != null) {
			Invocation.Builder invocationBuilder = webTarget.path(path).request(MediaType.APPLICATION_JSON)
					.header(jbpmAuth, jbpmAuthType + authToken);
			response = invocationBuilder.delete();
		}
		return response;
	}

	public Response getRequest(String authToken, String path, HashMap<String, Object> queryParams) {
		Response response = null;
		try {
			getClientBuilder();
			WebTarget taskWebTarget = webTarget.path(path);
			for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
				taskWebTarget = webTarget.path(path).queryParam(entry.getKey(), entry.getValue());
			}
			
			Invocation.Builder invocationBuilder = taskWebTarget.request(MediaType.APPLICATION_JSON).header(jbpmAuth,
					jbpmAuthType + authToken);
			response = invocationBuilder.get();
			System.out
					.println("===============================Get Response=====================" + response.getStatus());

		} catch (Exception e) {
			logger.error("Exception Occured in getRequest Method------------"+e.getMessage(),e);		}
		
		return response;
	}

	public Response postRequest(String authToken, String path, HashMap<String, Object> queryParams,
			String jsonPayLoad) {
		String cookieValue = getCookieValue();
		WebTarget taskWebTarget = webTarget.path(path);
		for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
			taskWebTarget = taskWebTarget.queryParam(entry.getKey(), entry.getValue());
		}
		Invocation.Builder invocationBuilder = taskWebTarget.request(MediaType.APPLICATION_JSON).header(jbpmAuth,
				jbpmAuthType + authToken);
		//logger.info("***************cookieValue**************" + cookieValue);
		Response response = invocationBuilder.cookie("Cookie", cookieValue)
				.post(Entity.entity(jsonPayLoad, MediaType.APPLICATION_JSON));
		//logger.info("===============================POST Response=====================" + response.getStatus());
		return response;
	}

}
