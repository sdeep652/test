package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.client.KeyCloakClient;
import com.tcts.foresight.entity.AutoClosureDetailsEntity;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.GroupDetailsEntity;
import com.tcts.foresight.entity.PageNamesDetailsEntity;
import com.tcts.foresight.entity.RoleDetailsEntity;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.pojo.UserDetailsDTO;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.RoleSummary;
import com.tcts.foresight.repository.UserDetailsSummary;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.service.UserManagementService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/user")
public class UserManagementController {
	Logger logger = LoggerFactory.getLogger(UserManagementController.class);
	
	@Autowired
	private UserManagementService userMngService;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private KeyCloakClient keyCloakClnt;
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	GroupDetailsRepository lGroupDetailsRepository;

	@GetMapping("/pageAccessList/{module}")
	public List<PageNamesDetailsEntity> getPageAccessList(@PathVariable String module) {
		return userMngService.getPageAccessList(module);
	}
	
	@GetMapping("/pageAccessList")
	public List<PageNamesDetailsEntity> getPageAccessList() {
		return userMngService.getPageAccessList();
	}

	@GetMapping("getPageAccessbyRole/{module}/{roles}")
	public List<PageNamesDetailsEntity> getPageAccessbyRoles(@PathVariable String module,
			@PathVariable List<String> roles) {
		return userMngService.getPageAccessbyRoles(module, roles);
	}

	@PostMapping("/create")
	public @Valid UserDetailsEntity createUser(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody UserDetailsEntity userReq) {
		try {
			logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(userReq));

			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_user)) {
				userMngService.createUser(userReq);
			} else {
				UserDetailsEntity user1 = new UserDetailsEntity();
				user1.setMessage(Constant.User_Does_Not_Have_Authorization);
				return user1;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in createUser " + e.getMessage(),e);
		}
		return userReq;
	}

	@GetMapping("/userslist")
	public List<UserDetailsEntity> getAllUser() {
		return userMngService.getAllUser();
	}

	@PutMapping("/updateUser/{userId}")
	public UserDetailsEntity updateUser(@PathVariable Long userId, @Valid @RequestBody UserDetailsEntity userReq) {
		UserDetailsEntity userObj = null;
		try {
			logger.info("Update User:- " + objMapper.writeValueAsString(userReq));
			userObj = userMngService.updateUser(userId, userReq);
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in updateUser " + e.getMessage(),e);
		} catch (ResourceNotFoundException e) {
			logger.error("Exception occur while in updateUser " + e.getMessage(),e);
		}catch (Exception e) {
			logger.error("Exception occur while in updateUser " + e.getMessage(),e);
		}
		return userObj;
	}
		
	@PutMapping("/resetUserPassword")
	public HashMap<String, String> resetUserPasswd(@Valid @RequestBody UserDetailsEntity userReq ) throws JsonProcessingException {
		 //logger.info("Update User:- " + userReq.getPassword());
	     HashMap<String, String> map = userMngService.resetUserPasswd(userReq);
		return map;
	}
	
	
	@PutMapping("/resetUserPasswordAdmin")
	public UserDetailsEntity resetUserPasswdAdmin(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody UserDetailsEntity userReq) throws JsonProcessingException {
		
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(userReq));

			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_user)) {
				//logger.info("Hello " + userReq.getEmail());
				userReq.setNewpass(Constant.Default_Password);
				//logger.info("Update User:- " + userReq.getPassword());
				map = userMngService.resetUserPasswd(userReq);
				userReq = userRepo.findByEmail1(userReq.getEmail());
			} else {
				UserDetailsEntity user1 = new UserDetailsEntity();
				user1.setMessage(Constant.User_Does_Not_Have_Authorization);
				return user1;
		}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in resetUserPasswdAdmin " + e.getMessage(),e);
		}
		return userReq;
	}
	
	
	
	@PutMapping("updateTheme/{userTheme}/{userName}")
	public ResponseEntity<UserDetailsEntity> updateTheme(@PathVariable String userTheme,
			@PathVariable String userName) {
		UserDetailsEntity userDetails = null;
		try {
			userDetails = userMngService.updateTheme(userTheme, userName);
		} catch (ResourceNotFoundException e) {
			logger.error("Exception occur while in updateTheme " + e.getMessage(),e);
		}
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
	}

	@GetMapping("/fetchUserName/{userName}")
	public String fetchUserName(@PathVariable String userName) {
		return userMngService.fetchUserName(userName);
	}

	@GetMapping("/fetchUserData/{userName}")
	public UserDetailsEntity fetchUserDataByEmail(@PathVariable String userName) {
		return userMngService.fetchUserDataByEmail(userName.toLowerCase());
	}

	@GetMapping("/fetchUserByGroup/{groupId}/{groupName}")
	public List<UserDetailsSummary> fetchUserByGroup(@PathVariable String groupId, @PathVariable String groupName) {
		return userMngService.fetchUserByGroup(groupId, groupName);
	}

	@PostMapping("/fetchUserByMultipleGroup")
	public Set<UserDetailsDTO> fetchUserByMultipleGroup(@RequestBody List<GroupDetailsEntity> jsonPayLoad) {
		return userMngService.fetchUserByMultipleGroup(jsonPayLoad);
	}

	@GetMapping("/fetchGroupListByEmail/{userName}")
	public Set<GroupSummary> fetchGroupListByEmail(@PathVariable String userName) {
		return userMngService.fetchGroupListByEmail(userName);
	}

	@GetMapping("/fetchGroupListByEmailAndModule/{userName}/{module}")
	public List<GroupSummary> fetchGroupListByEmailAndModule(@PathVariable String userName,
			@PathVariable String module) {
		return userMngService.fetchGroupListByEmailAndModule(userName, module);
	}

	@GetMapping("/getRolesByUserName/{userName}")
	public UserDetailsDTO getRolesByUserName(@PathVariable String userName) {
		UserDetailsDTO userDetailsDTO = null;

		try {
			userDetailsDTO = userMngService.getRolesByUserName(userName);
			logger.info("Fetched User:- " + objMapper.writeValueAsString(userDetailsDTO));
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in getRolesByUserName " + e.getMessage(),e);
		}
		return userDetailsDTO;
	}

	@DeleteMapping("/delete/{id}/{userId}")
	public String deleteUser(@RequestHeader("Authorization") String authToken, @PathVariable Long id,
			@PathVariable String userId) {

		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_user)) {
				userMngService.deleteUser(id, userId);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in deleteUser " + e.getMessage(),e);
		}
		return "User Deleted Successfully";
	}
	
	@GetMapping("/userlistbyrole/{roleName}")
	public List<String> getUserListByRole(@PathVariable("roleName") String roleName) {
		//logger.info("Role Name:- " + roleName);
		return userMngService.getUserListByRole(roleName);
	}

	@GetMapping("/roleNameByUserName/{userName}")
	public List<String> getRoleNameByUserName(@PathVariable("userName") String userName) {
		return userMngService.getRoleNameByUserName(userName);
	}

	@GetMapping("/roleList")
	public List<String> getRoleList() {
		return userMngService.getRoleList();
	}

	// Added By kshitiz
	@GetMapping("/groupList")
	public List<String> getGroupList() {
		return userMngService.getRoleList();
	}

	@GetMapping("/usersdetails/{userName}")
	public List<UserDetailsEntity> getUserDetails(@PathVariable("userName") String userName) {
		return userMngService.getUserDetails(userName);
	}

	@GetMapping("/userslist1")
	public List<String> getUserLists() {
		return userMngService.getUserLists();
	}

	/* Creating Roles In KeyCloak using GroupAPI */
	@GetMapping("/checkDupRole/{roleName}")
	public String checkDupRole(@PathVariable String roleName) {
		return userMngService.checkDupRole(roleName);
	}

	@PostMapping("/createrole")
	public RoleDetailsEntity createRole(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody RoleDetailsEntity role) {
		try {
			//logger.info("Role Creation:- " + role.getRoleName());
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_role)) {
				userMngService.createRole(role);
			} else {
				RoleDetailsEntity rol = new RoleDetailsEntity();
				rol.setMessage(Constant.User_Does_Not_Have_Authorization);
				return rol;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in createRole " + e.getMessage(),e);
		}
		return role;
	}

	@PutMapping("/updateRole/{roleId}")
	public RoleDetailsEntity updateRole(@RequestHeader("Authorization") String authToken, @PathVariable String roleId,
			@Valid @RequestBody RoleDetailsEntity roleDtls) {
		try {
			//logger.info("Updating Role Id :- " + roleDtls.getId() + " Role Name " + roleDtls.getRoleName());
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_role)) {
				userMngService.updateRole(roleId, roleDtls);
			} else {
				RoleDetailsEntity rol = new RoleDetailsEntity();
				rol.setMessage(Constant.User_Does_Not_Have_Authorization);
				return rol;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in updateRole " + e.getMessage(),e);
		}
		return roleDtls;
	}

	@GetMapping("/fetchRoleList")
	public List<RoleDetailsEntity> fetchRoleList() {
		return userMngService.fetchRoleList();
	}

	@GetMapping("/fetchOnlyRole")
	public List<RoleSummary> fetchOnlyRole() {
		return userMngService.fetchOnlyRole();
	}

	/* Creating Groups In KeyCloak using RoleAPI */
	@GetMapping("/checkDupGroup/{groupName}")
	public String checkDupGroup(@PathVariable String groupName) throws ResourceNotFoundException {
		return userMngService.checkDupGroup(groupName);
	}

	@PostMapping("/creategroup")
	public GroupDetailsEntity createGroupAsRole(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody GroupDetailsEntity groupDtls) {
		GroupDetailsEntity returnGroupDetailsEntity=new GroupDetailsEntity();
		try {
			//logger.info("Group Creation:- " + groupDtls.getGroupName());
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
				returnGroupDetailsEntity=userMngService.createGroupAsRole(groupDtls);
			} else {
				GroupDetailsEntity Grp = new GroupDetailsEntity();
				Grp.setMessage(Constant.User_Does_Not_Have_Authorization);
				return Grp;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in createGroupAsRole " + e.getMessage(),e);
		}
		return returnGroupDetailsEntity;

	}

	@PutMapping("/updateGroup/{groupId}")
	public GroupDetailsEntity updateGroupAsRole(@RequestHeader("Authorization") String authToken,
			@PathVariable String groupId, @Valid @RequestBody GroupDetailsEntity groupDtls) {
		try {
			//logger.info("Group Creation:- " + groupDtls.getGroupName());
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
				userMngService.updateGroupAsRole(groupId, groupDtls);
			} else {
				GroupDetailsEntity Grp = new GroupDetailsEntity();
				Grp.setMessage(Constant.User_Does_Not_Have_Authorization);
				return Grp;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in updateGroupAsRole " + e.getMessage(),e);
		}
		return groupDtls;
	}

	@PutMapping("/updateGroupDefault/{groupId}")
	public GroupDetailsEntity updateGroupAsDefault(@RequestHeader("Authorization") String authToken,
			@PathVariable String groupId) {
		GroupDetailsEntity grpDtls = null;
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
				grpDtls = userMngService.updateGroupAsDefault(groupId);
				return grpDtls;
			} else {
				GroupDetailsEntity Grp = new GroupDetailsEntity();
				Grp.setMessage(Constant.User_Does_Not_Have_Authorization);
				return Grp;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in updateGroupAsDefault " + e.getMessage(),e);
		}
		return grpDtls;

	}
	
	@PutMapping("/updateDefaultModuleGroup/{groupId}")
	public ResponseEntity<Object> updateGroupModuleAsDefault(@RequestHeader("Authorization") String authToken,@PathVariable String groupId,
			@RequestBody GroupDetailsEntity lGroupDetailsEntity) {
		
		GroupDetailsEntity grpDtls = null;
		
		try {
			
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
				List<String> moduleList = lGroupDetailsRepository.findModulebyGroupname(groupId);
				List<String> modulePayLoad = Stream.of(lGroupDetailsEntity.getDefaultModuleGroup().split(",", -1)).collect(Collectors.toList());

				if(! moduleList.isEmpty()) {
				if(moduleList.containsAll(modulePayLoad)) {
					grpDtls = userMngService.updateGroupModuleAsDefault(groupId,lGroupDetailsEntity.getDefaultModuleGroup());
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(grpDtls);
				}
				else { 
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorDetails("200", "No Associated module", "No Associated module"));
				}
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorDetails("200", "No Associated module", "No Associated module"));
			}
			} else {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in updateDefaultModuleGroup: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}

	}

	//Original code commented for testing 
	
//	@PutMapping("/updateDefaultModuleGroup/{groupId}/{module}")
//	public GroupDetailsEntity updateGroupModuleAsDefault(@RequestHeader("Authorization") String authToken,
//			@PathVariable String groupId, @PathVariable String module) {
//		GroupDetailsEntity grpDtls = null;
//		try {
//			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
//				grpDtls = userMngService.updateGroupModuleAsDefault(groupId,module);
//				return grpDtls;
//			} else {
//				GroupDetailsEntity Grp = new GroupDetailsEntity();
//				Grp.setMessage(Constant.User_Does_Not_Have_Authorization);
//				return Grp;
//			}
//		} catch (Exception e) {
//			logger.error("Exception occur while in updateGroupAsDefault " + e.getMessage(),e);
//		}
//		return grpDtls;
//
//	}
//
	@GetMapping("/fetchGroupList")
	public List<GroupDetailsEntity> fetchGroupList() {
		return userMngService.fetchGroupList();
	}

	@GetMapping("/fetchOnlyGroup")
	public List<GroupSummary> fetchOnlyGroup() {
		return userMngService.fetchOnlyGroup();
	}

	@GetMapping("/fetchOnlyGroup/{module}")
	public List<GroupSummary> fetchOnlyGroup(@PathVariable String module) {
		return userMngService.fetchOnlyGroup(module);
	}

	@GetMapping("/fetchPagesByAuthToken/{authToken}/{pageName}")
	public boolean fetchPagesByAuthToken(@PathVariable String authToken, @PathVariable String pageName) {
		return userMngService.fetchPagesByAuthToken(authToken, pageName);
	}

	/* Deleting KeyCloak Roles as Group */
	@DeleteMapping("/deleteGroup/{groupId}/{groupName}")
	public String deleteGroup(@RequestHeader("Authorization") String authToken, @PathVariable String groupId,
			@PathVariable String groupName) {
		String delGrp = null;
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_group)) {
				delGrp = userMngService.deleteGroup(groupId, groupName);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in deleteGroup " + e.getMessage(),e);
		}
		return delGrp;
	}

	@DeleteMapping("/deleteRole/{roleId}/{roleName}")
	public String deleteRole(@RequestHeader("Authorization") String authToken, @PathVariable String roleId,
			@PathVariable String roleName) {
		String delRole = null;
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.UM_Adminconfig_role)) {
				delRole = userMngService.deleteRole(roleId, roleName);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in deleteRole " + e.getMessage(),e);
		}
		return delRole;
	}
	
	
	@PostMapping("/createAutoClose")
	public AutoClosureDetailsEntity createAutoCloseRule(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody AutoClosureDetailsEntity autoCloseDtls) {
		AutoClosureDetailsEntity returnedAutoCloseDtls = null;
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_autoclose)) {
				logger.info("Create Auto Close Rule Json Body:- " + objMapper.writeValueAsString(autoCloseDtls));
				returnedAutoCloseDtls = userMngService.createAutoCloseRule(autoCloseDtls);
			} else {
				AutoClosureDetailsEntity acd = new AutoClosureDetailsEntity();
				acd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return acd;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("Exception occur while in createAutoCloseRule: " + e.getMessage(),e);
		}
		return returnedAutoCloseDtls;
	}

	@PutMapping("/updateAutoClose/{autoCloseRuleId}")
	public AutoClosureDetailsEntity updateAutoCloseRule(@RequestHeader("Authorization") String authToken,
			@PathVariable Long autoCloseRuleId, @Valid @RequestBody AutoClosureDetailsEntity autoCloseDtls) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_autoclose)) {
				userMngService.updateAutoCloseRule(autoCloseRuleId, autoCloseDtls);
			} else {
				AutoClosureDetailsEntity acd = new AutoClosureDetailsEntity();
				acd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return acd;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in updateAutoCloseRule " + e.getMessage(),e);
		}
		return autoCloseDtls;
	}

	@GetMapping("/fetchAllAutoCloseRules/{module}")
	public List<AutoClosureDetailsEntity> fetchAllAutoCloseRules(@PathVariable String module) {
		return userMngService.fetchAllAutoCloseRules(module);
	}

	@GetMapping("/fetchAutoCloseRule/{catId}/{priorityName}/{module}")
	public AutoClosureDetailsEntity fetchAutoCloseRule(@PathVariable Long catId, @PathVariable String priorityName,@PathVariable String module){
		return userMngService.fetchAutoCloseRule(catId,priorityName,module);
	}
	
	@GetMapping("/fetchAutoCloseTime/{catId}/{priorityName}/{module}")
	public String fetchAutoCloseTime(@PathVariable Long catId, @PathVariable String priorityName,@PathVariable String module){
		return userMngService.fetchAutoCloseTime(catId,priorityName,module);
	}

	@DeleteMapping("deleteAutoClose/{autoCloseRuleId}")
	public String deleteAutoCloseRule(@RequestHeader("Authorization") String authToken,
			@PathVariable Long autoCloseRuleId) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_autoclose)) {
				userMngService.deleteAutoCloseRule(autoCloseRuleId);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in deleteAutoCloseRule " + e.getMessage(),e);
		}
		return "User Deleted Successfully";
	}
	
	
	@GetMapping("/fetchAllUserNameAndGroupDetails/")
	public List<HashMap<String, Object>> fetchAllUserNameAndGroupDetails(){
		return userMngService.fetchAllUserNameAndGroupDetails();
	}

	@PostMapping("/fetchFullNameOnUserId")
	public List<HashMap<String, String>> fetchFullNameOnUserId(@RequestBody String payload) {
		
		return userMngService.fetchFullNameOnUserId(payload);

	}
	
	@GetMapping("/syncuserswithpostgres")
	public List<UserDetailsEntity> syncUsersWithPostgres(){
		List<UserDetailsEntity> completeUserRepObj = new ArrayList<UserDetailsEntity>();
		try {
		completeUserRepObj = userMngService.syncUsersWithPostgres();
		return completeUserRepObj;
		}catch(Exception e) {
			logger.info("Exception occurred in syncuserswithpostgres "+e);
		}
		return completeUserRepObj;
		
		
	}
			
}
