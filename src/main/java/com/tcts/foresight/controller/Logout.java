package com.tcts.foresight.controller;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.client.KeyCloakClient;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("log/")
public class Logout {

	@Autowired	
	private KeyCloakClient keycloakClient;

	private static final  Logger logoutLog = LogManager.getLogger(Logout.class.getName());
	public Logout() {
		super();
	}

	@ApiOperation(value = "Log out user from keycloak", response = Response.class)
	@RequestMapping(value = "logout-user/{username}", method = RequestMethod.POST, produces =MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Response logout(@PathVariable("username") String username) throws Exception {
		String userId=null;
		Response response2=null;
		
		String realm = null;
		
		Keycloak keycloak = keycloakClient.getkeycloakInstance();
		realm = keycloakClient.getKeyClkRealm();

		AccessTokenResponse accessTokenObject = keycloak.tokenManager().getAccessToken();
		String accessToken = accessTokenObject.getToken();
		
		List<UserRepresentation> completeUserRepObj = keycloak.realm(keycloakClient.getKeyClkRealm()).users().search(username);

		for(UserRepresentation obj:completeUserRepObj)
		{
			if(username.equalsIgnoreCase(obj.getUsername()))
			{
				userId = obj.getId();
			}
		}
		String pathForLogout="/admin/realms/"+realm+"/users/"+userId+"/logout";
		response2 = keycloakClient.postRequestForBearerAuth(accessToken, pathForLogout, "");
		logoutLog.info("User Logged Out {}",response2.getStatusInfo());

		return response2;
	} 


}
