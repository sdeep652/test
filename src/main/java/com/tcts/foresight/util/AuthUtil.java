package com.tcts.foresight.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcts.foresight.client.JBPMClientConfig;
import com.tcts.foresight.controller.AuthControllers;
import com.tcts.foresight.service.UserManagementService;
import com.tcts.foresight.service.impl.IncidentCreationServiceImpl;
import com.tcts.foresight.service.impl.problem.ProblemManagementImpl;
@Component
public class AuthUtil {
	Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	@Autowired
	private JBPMClientConfig jBPMClientConfig;
	
	@Autowired
	private UserManagementService userMngService;
	
	@Autowired
	ProblemManagementImpl lProblemManagementImpl;
	
	@Autowired
	IncidentCreationServiceImpl lIncidentCreationServiceImpl;
	
	public static String createAuthToken(String username, String password) {
		Logger logger = LoggerFactory.getLogger(AuthControllers.class); 
		String authToken = null;
		String toBeEncoded = username + ":" + password;
		try {
			authToken = Base64.getEncoder().encodeToString(toBeEncoded.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("------------"+e.getMessage());
			logger.error("Exception occur while in Create Auth Token"+e.getMessage(),e);
     	}
		return authToken;
	}

	
	public boolean authenticateAndAuthorizeCheck(String authToken, String pageName)
	{
		
		Response response = jBPMClientConfig.getRequest(authToken, "");
		if(response.getStatus()==200)
		{
			if(userMngService.fetchPagesByAuthToken(authToken,pageName))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean authenticateAndAuthorizeCheck(String authToken, String pageName, String problemID)
	{
		
		Response response = jBPMClientConfig.getRequest(authToken, "");
		if(response.getStatus()==200)
		{
			if(userMngService.fetchPagesByAuthToken(authToken,pageName))
			{
				
				if(StringUtil.isNotNullNotEmpty(problemID))
				{
					String assignedToFromTicket = getAssignToFromProblemTicket(problemID);
					String username = getUserNameFromAuthToken(authToken);
					if(assignedToFromTicket.equals(username))
					{
						return true;
					}
				}
				
				
			}
		}
		return false;
	}
	
	
	public boolean authenticateAndAuthorizeCheckForIncidentUpdate(String authToken, String pageName, String incidentID)
	{
		
		Response response = jBPMClientConfig.getRequest(authToken, "");
		if(response.getStatus()==200)
		{
			if(userMngService.fetchPagesByAuthToken(authToken,pageName))
			{
				
				if(StringUtil.isNotNullNotEmpty(incidentID))
				{
					String assignedToFromTicket = getAssignToFromIncidentTicket(incidentID);
					String username = getUserNameFromAuthToken(authToken);
					if(assignedToFromTicket.equals(username))
					{
						return true;
					}
				}
				
				
			}
		}
		return false;
	}
	
	private String getUserNameFromAuthToken(String authToken) {
		
		String username = userMngService.getUserNameFromAuth(authToken);
		System.out.println("Username" +username);
		return username;
	}

	private String getAssignToFromProblemTicket(String problemID) {
	String assignedToFromTicket = lProblemManagementImpl.getAssignToFromProblemTicket(problemID);
		return assignedToFromTicket;	
	}

	private String getAssignToFromIncidentTicket(String incidentID) {
		String assignedToFromTicket = lIncidentCreationServiceImpl.getAssignToFromIncidentTicket(incidentID);
			return assignedToFromTicket;	
		}
	public boolean isValidCredential(String username, String password)
	{
		if(username!=null && password!=null)
		{
			String authToken = createAuthToken(username, password);
			Response response = jBPMClientConfig.getRequest(authToken, "");
			//logger.info("response.getStatus() :"+response.getStatus());
			if(response.getStatus()==200)
			{	
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
}
