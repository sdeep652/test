package com.tcts.foresight.client;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakClient {

	private static final Logger keyCloakClntLog = LogManager.getLogger(KeyCloakClient.class.getName());
	
	private WebTarget webTarget=null;

	private String keyClkSrvrUrl;
	private String keyClkUserName;
	private String keyClkPwd;
	private String keyClkRealm;
	private String keyClkClientId;

	@Autowired
	public KeyCloakClient(@Value("${keycloak.server.url}") String keyClkSrvrUrl,
			@Value("${keycloak.username}") String keyClkUserName,
			@Value("${keycloak.password}") String keyClkPwd,
			@Value("${keycloak.realm}") String keyClkRealm,
			@Value("${keycloak.clientid}") String keyClkClientId) {
		
		this.keyClkSrvrUrl = keyClkSrvrUrl;
		this.keyClkUserName = keyClkUserName;
		this.keyClkPwd = keyClkPwd;
		this.keyClkRealm = keyClkRealm;
		this.keyClkClientId = keyClkClientId;
		
		Client client =ClientBuilder.newClient();
		this.webTarget = client.target(this.keyClkSrvrUrl);
	}
	
	
	public String getKeyClkSrvrUrl() {
		return keyClkSrvrUrl;
	}

	public void setKeyClkSrvrUrl(String keyClkSrvrUrl) {
		this.keyClkSrvrUrl = keyClkSrvrUrl;
	}

	public String getKeyClkUserName() {
		return keyClkUserName;
	}

	public void setKeyClkUserName(String keyClkUserName) {
		this.keyClkUserName = keyClkUserName;
	}

	public String getKeyClkPwd() {
		return keyClkPwd;
	}

	public void setKeyClkPwd(String keyClkPwd) {
		this.keyClkPwd = keyClkPwd;
	}

	public String getKeyClkRealm() {
		return keyClkRealm;
	}

	public void setKeyClkRealm(String keyClkRealm) {
		this.keyClkRealm = keyClkRealm;
	}

	public String getKeyClkClientId() {
		return keyClkClientId;
	}

	public void setKeyClkClientId(String keyClkClientId) {
		this.keyClkClientId = keyClkClientId;
	}

	public Keycloak getkeycloakInstance()
	{	
		Keycloak keycloakClient =null;
		try {
			
			keycloakClient = Keycloak.getInstance(keyClkSrvrUrl, keyClkRealm, keyClkUserName, keyClkPwd, keyClkClientId);
			keyCloakClntLog.info("KeyCloak Builder:- "+KeycloakBuilder.builder().serverUrl(keyClkSrvrUrl).realm(keyClkRealm).username(keyClkUserName).password(keyClkPwd).clientId(keyClkClientId).build().realm(keyClkRealm));

		}catch (Exception e) {
			keyCloakClntLog.error("Exception occured in keycloak: " + e.getMessage(), e);  
			throw e;
		}
		return keycloakClient;
	}

	public Response postRequestForNoAuth(String path,String requestPayload){
		WebTarget taskWebTarget = webTarget.path(path);
		Invocation.Builder invocationBuilder = taskWebTarget.request(MediaType.APPLICATION_JSON).header("Content-Type", "application/x-www-form-urlencoded");
		Response response = invocationBuilder.post(Entity.entity(requestPayload, MediaType.APPLICATION_FORM_URLENCODED));
		return response;
	}

	public Response postRequestForBearerAuth(String authToken,String path,String requestPayload){
		WebTarget taskWebTarget = webTarget.path(path);
		Invocation.Builder invocationBuilder = taskWebTarget.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer "+authToken);
		Response response = invocationBuilder.post(Entity.entity(requestPayload, MediaType.APPLICATION_JSON));
		return response;
	}


	public Response getRequestForBearerAuth(String authToken, String path, HashMap<String, Object> queryParams){
		WebTarget taskWebTarget = webTarget.path(path);
		for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
			
			taskWebTarget = taskWebTarget.queryParam(entry.getKey(), entry.getValue());
		}
		
		Invocation.Builder invocationBuilder = taskWebTarget.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic "+authToken);
		Response response = invocationBuilder.get();
		return response;

	}

}

