package com.tcts.foresight.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.client.JBPMClientConfig;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.pojo.Login;
import com.tcts.foresight.pojo.RoleDetailsDTO;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.service.UserManagementService;
import com.tcts.foresight.service.impl.UserManagementServiceImpl;
import com.tcts.foresight.util.AuthUtil; 

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AuthControllers {

	Logger logger = LoggerFactory.getLogger(AuthControllers.class); 

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JBPMClientConfig jBPMClientConfig;

	@Autowired
	private UserManagementService userMngService;
	
	@Autowired
	private UserManagementServiceImpl lUserManagementServiceImpl;
	
	
	
	@CrossOrigin
	@PostMapping("/login")
	@ResponseBody
	public Object userLogin(@RequestBody Login userLogin, HttpServletRequest request) {
		String authToken = AuthUtil.createAuthToken(userLogin.getUsername(), userLogin.getPassword());
		String fullName =  userRepo.findFullNameByEmail(userLogin.getUsername());
		Response response = jBPMClientConfig.getRequest(authToken, "");
		String userTheme = userMngService.getUserTheme(userLogin.getUsername().toLowerCase());
		List<String> module = userRepo.findModulebyUsername(userLogin.getUsername().toLowerCase());
		HttpSession httpSession = request.getSession(true);
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		Login login = new Login();
		if (response.getStatus() == 200) {
			List<RoleDetailsDTO> roleDtls=userMngService.getRolesByUserName(userLogin.getUsername().toLowerCase()).getRoleList();
			responseMap.put("token",authToken);
			responseMap.put("username", userLogin.getUsername());
			responseMap.put("roleDtls", roleDtls);
			responseMap.put("fullName", fullName);
			responseMap.put("module", module);

			if(userTheme == null) {
				responseMap.put("userTheme", "Default");
			}else {
				responseMap.put("userTheme", userTheme);
			}
			httpSession.setAttribute("Authorization", authToken);
			httpSession.setAttribute("USER_NAME", userLogin.getUsername());
			login.setToken(authToken);
			login.setUsername(userLogin.getUsername());
			
			//added code for Reset OTP
			UserDetailsEntity lUserDetailsEntity=lUserManagementServiceImpl.fetchUserDataByEmail(userLogin.getUsername());
			lUserDetailsEntity.setOtp_count(0L);
			userRepo.save(lUserDetailsEntity);
			

			//logger.info("responseMap : "+responseMap);
			response = Response.status(Response.Status.OK).header("Authorization", "Basic "+authToken).entity(responseMap).build();
			//logger.info("response : "+response);
			return response.getEntity();

		} else {
			responseMap.put("status",response.getStatus());
			responseMap.put("status info",response.getStatusInfo());
			response.close();
			//logger.info("response status:- "+ response.getStatus());
			//logger.info("response status infor :- "+ response.getStatusInfo());
			//logger.info("response headers:- "+ response.getStringHeaders());
			response = Response.status(response.getStatus()).header("Content-Type", MediaType.APPLICATION_JSON)
					.entity(responseMap).build();
			//logger.info("response entity:- "+ response.getEntity());
			
		} 
		return response.getEntity();
	}

}
