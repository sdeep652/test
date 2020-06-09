package com.tcts.foresight.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

import com.tcts.foresight.client.KeyCloakClient;
import com.tcts.foresight.entity.AutoClosureDetailsEntity;
import com.tcts.foresight.entity.GroupDetailsEntity;
import com.tcts.foresight.entity.PageNamesDetailsEntity;
import com.tcts.foresight.entity.RoleDetailsEntity;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.pojo.RoleDetailsDTO;
import com.tcts.foresight.pojo.UserDetailsDTO;
import com.tcts.foresight.repository.AutoClosureRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.PageAccessRepo;
import com.tcts.foresight.repository.PageSummary;
import com.tcts.foresight.repository.RoleDetailsRepository;
import com.tcts.foresight.repository.RoleSummary;
import com.tcts.foresight.repository.UserDetailsSummary;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.service.UserManagementService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.StringUtil;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {
	Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PageAccessRepo pageAccessRepo;

	@Autowired
	private RoleDetailsRepository roleDtlsRepo;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;

	@Autowired
	private AutoClosureRepo autoClosureRepo;

	@Autowired
	private KeyCloakClient keyCloakClnt;

	@Autowired
	@Qualifier("auxDataSource")
	private DataSource auxDataSource;

	@Autowired
	private AuthUtil authUtil;

	@Override
	public List<PageNamesDetailsEntity> getPageAccessList(String module) {
		return pageAccessRepo.findAll().stream().filter(pageName -> pageName.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<PageNamesDetailsEntity> getPageAccessList() {
		return pageAccessRepo.findAll();
	}

	@Override
	public List<PageNamesDetailsEntity> getPageAccessbyRoles(String module, List<String> roles) {
		//logger.info(roles + "_______" + module);
		return pageAccessRepo.findPageAccessByRoleName(module, roles);
	}

	@Override
	public List<UserDetailsEntity> bulkUserCreation(List<UserDetailsEntity> bulkUserList) {
		List<UserDetailsEntity> retrnbulkUserList = null;

		retrnbulkUserList = bulkUserList.stream().filter((user) -> user.getEmail() != null)
				.filter((user) -> user.getFirstName() != null).filter((user) -> user.getLastName() != null)
				.map((user) -> {
					user.setGroupList(new ArrayList<>());
					return createUser(user);
				}).filter(returnedUser -> returnedUser != null && returnedUser.getUserId() != null)
				.collect(Collectors.toList());
		//logger.info("Returned Users List Size :- " + retrnbulkUserList.size());

		retrnbulkUserList.stream().forEach(action -> logger.info("Printing Name:- " + action.getFirstName()));

		return retrnbulkUserList;
	}

	@Override
	public UserDetailsEntity createUser(UserDetailsEntity user) {
		UserDetailsEntity returnUser = null;
		List<RoleRepresentation> roleRepListToAdd = new LinkedList<RoleRepresentation>();
		Response response = null;
		String userId = null;
		try {

			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setUsername(user.getEmail());
			userRepresentation.setEmail(user.getEmail());
			userRepresentation.setFirstName(user.getFirstName());
			userRepresentation.setLastName(user.getLastName());
			userRepresentation.setEnabled(true);

			CredentialRepresentation rawPassword = new CredentialRepresentation();
			if (user.getPassword() == null) {
				user.setPassword(Constant.Default_Password);
			}
			rawPassword.setValue(user.getPassword());
			rawPassword.setType(CredentialRepresentation.PASSWORD);
			rawPassword.setTemporary(false);

			List<CredentialRepresentation> credentialList = new ArrayList<CredentialRepresentation>();
			credentialList.add(rawPassword);
			userRepresentation.setCredentials(credentialList);
			response = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users()
					.create(userRepresentation);
			//logger.info("Group List:- " + user.getGroupList().size());
			if (response.getStatusInfo().equals(Status.CREATED)) {
				userId = getCreatedId(response);

				//logger.info("KeyCloak Created UserId:- " + userId);

				for (GroupDetailsEntity l : user.getGroupList()) {
					//logger.info("Key Cloak RoleName from List :-" + l.getGroupName());

					RoleRepresentation roleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
							.roles().get(l.getGroupName()).toRepresentation();
					//logger.info("Hash Code of the Object:- " + roleRep.hashCode());
					//logger.info("KeyCloak Role Name:- " + roleRep.getName());

					if (roleRep.isComposite()) {
						roleRepListToAdd.add(roleRep);
					}

				}
				keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userId).roles()
				.realmLevel().add(roleRepListToAdd);
				user.setUserId(userId);
				user.setAuthToken(AuthUtil.createAuthToken(user.getEmail(), user.getPassword()));
				returnUser = userRepo.save(user);
			}
			//logger.info("Created User Return:- " + returnUser);

		} catch (ConstraintViolationException conViolatnEx) {
			conViolatnEx.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception Occurred in createUser impl: " + e.getMessage(),e);
		}
		return returnUser;
	}

	@Override
	public @Valid UserDetailsEntity updateUser(Long userId, @Valid UserDetailsEntity userReq)
			throws ResourceNotFoundException {

		List<RoleRepresentation> roleRepListToAdd = new LinkedList<RoleRepresentation>();

		if (userReq.getUserId() != null) {

			//logger.info("Update KeyCloak Data:- " + userReq.getUserId());
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setUsername(userReq.getEmail());
			userRepresentation.setEmail(userReq.getEmail());
			userRepresentation.setFirstName(userReq.getFirstName());
			userRepresentation.setLastName(userReq.getLastName());
			userRepresentation.setEnabled(true);

			// deleting associated groups
			List<RoleRepresentation> userRepListTodelete = new LinkedList<RoleRepresentation>();
			userRepListTodelete = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users()
					.get(userReq.getUserId()).roles().realmLevel().listEffective();

			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userReq.getUserId())
			.roles().realmLevel().remove(userRepListTodelete);

			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userReq.getUserId())
			.update(userRepresentation);
			// Adding New Groups given from UI
			for (GroupDetailsEntity l : userReq.getGroupList()) {
				RoleRepresentation roleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
						.roles().get(l.getGroupName()).toRepresentation();
				//logger.info("Hash Code of the Object:- " + roleRep.hashCode());
				roleRepListToAdd.add(roleRep);
			}
			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userReq.getUserId())
			.roles().realmLevel().add(roleRepListToAdd);

			// Adding Default JBPM roles again
			RealmRepresentation Realm = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
					.toRepresentation();
			List<RoleRepresentation> DefaultList = new LinkedList<RoleRepresentation>();
			List<String> Defautroles = Realm.getDefaultRoles();
			for (String S : Defautroles) {
				RoleRepresentation Rep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
						.get(S.toString()).toRepresentation();
				DefaultList.add(Rep);
			}
			//logger.info("DefaultList------------------:" + DefaultList);
			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userReq.getUserId())
			.roles().realmLevel().add(DefaultList);

		}

		//get Authtoken as previously recorded in database
		UserDetailsEntity userdtls = userRepo.findByEmail1(userReq.getEmail());
		userReq.setAuthToken(userdtls.getAuthToken());

		// AuxDB Update
		return userRepo.findById(userId).map(user -> {
			return userRepo.save(userReq);
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

	@Override
	public HashMap<String, String> resetUserPasswd(@Valid UserDetailsEntity userReq) {
		HashMap<String, String> returnMap = new HashMap<String, String>();
		//	UserDetailsEntity list = null;

		boolean isOldCredentialValid =false;
		boolean isPAsswordResetFromAdmin = userReq.getIsPasswordResetRequiredByAdmin();
		if(isPAsswordResetFromAdmin==false)
		{
			isOldCredentialValid = authUtil.isValidCredential(userReq.getEmail(),userReq.getPassword());
		}
		if(isOldCredentialValid || isPAsswordResetFromAdmin)
		{	 
			returnMap = reserPasswordAtBackend(userReq);
		} else {
			returnMap.put("status", "fail");
			returnMap.put("remarks", "OLD UserName and/or Password are not matching");
		}
		return returnMap;
	}

	public HashMap<String, String> reserPasswordAtBackend(UserDetailsEntity userReq) {
		HashMap<String, String> returnMap = new HashMap<String, String>();
		UserDetailsEntity list = new UserDetailsEntity();
		try {
			String userId=null;
			List<UserRepresentation> completeUserRepObj = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().search(userReq.getEmail());
			for (UserRepresentation obj : completeUserRepObj) {
				if (userReq.getEmail().equalsIgnoreCase(obj.getUsername())) {
					userId = obj.getId();
				}
			} 
			//logger.info("data in user if "+userId);
			UserResource lUsersResource = (UserResource) keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userId);
			// fetch the "UserDetailsEntity based on userId"
			UserRepresentation lUserRepresentation = lUsersResource.toRepresentation();
			List<CredentialRepresentation> credentialList = new ArrayList<CredentialRepresentation>();
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(userReq.getNewpass());
			credential.setTemporary(false);
			credentialList.add(credential);
			lUserRepresentation.setCredentials(credentialList);
			// Remove the UPDATE_PASSWORD required action
			lUsersResource.update(lUserRepresentation);

			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().get(userId)
			.resetPassword(credential);
			// AuxDB Update		        
			list = userRepo.findByEmail1(userReq.getEmail());
			list.setAuthToken(AuthUtil.createAuthToken(userReq.getEmail(), userReq.getNewpass()));
			userRepo.save(list);
			returnMap.put("status", "success");
			returnMap.put("remarks", "Password is modified successfully");
		} catch (Exception e) {
			returnMap.put("status", "fail");
			returnMap.put("remarks", "Exceptions in password modifications, please try again later");
			e.printStackTrace();
			logger.error("Exception occured in reset password impl " + e.getMessage(), e);  
		}
		return returnMap;

	}

	@Override
	public UserDetailsEntity updateTheme(String userTheme, String userName) throws ResourceNotFoundException {
		return userRepo.findUserByEmail(userName).map(user -> {
			user.setUserTheme(userTheme);
			return userRepo.save(user);
		}).orElseThrow(() -> new ResourceNotFoundException("userName " + userName + " not found"));
	}

	@Override
	public List<UserDetailsEntity> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public String fetchUserName(String userName) {
		return userRepo.findByEmail(userName);
	}

	@Override
	public UserDetailsEntity fetchUserDataByEmail(String userName) {
		return userRepo.findUserByEmail(userName).get();
	}

	@Override
	public List<UserDetailsSummary> fetchUserByGroup(String groupId, String groupName) {
		
		List<UserDetailsSummary> list = userRepo.findUserByGroupList_IdAndGroupList_GroupName(groupId, groupName);
		
//		list.forEach((s)->System.out.println(s.getFullName()));    
		
		list.sort((UserDetailsSummary s1, UserDetailsSummary s2)->s1.getFullName().compareTo(s2.getFullName())); 
	    
//		list.forEach((s)->System.out.println(s.getFullName()));         

		return list;
	}

	@Override
	public UserDetailsDTO getRolesByUserName(String userName) {
		UserDetailsEntity userDetials = null;
		UserDetailsDTO userDetailsDTO = null;

		userDetials = userRepo.findUserByEmail(userName.toLowerCase()).get();
		userDetailsDTO = userDetials.getUserDetailsDTO();

		Set<RoleSummary> roleSummary = roleDtlsRepo.findRolesNameByUserName(userName);

//		List<RoleDetailsDTO> roleList = roleSummary.stream().filter(role -> role.getRoleName() != null)
//				.map(map -> new RoleDetailsDTO(map.getId(), map.getRoleName(), map.getModule()))
//				.collect(Collectors.toList());
		
		List<RoleDetailsDTO> roleList = roleSummary.stream().filter(role -> role.getRoleName() != null)
				.map(map -> new RoleDetailsDTO(map.getId(), map.getRoleName()))
				.collect(Collectors.toList());

		userDetailsDTO.setRoleList(roleList);
		return userDetailsDTO;
	}

	@Override
	public String deleteUser(Long id, String userId) {
		try {
			UserRepresentation userRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users()
					.get(userId).toRepresentation();
			//logger.info("Delete User Id:-" + id + " UserId:- " + userId);
			if (userRep.getId() != null) {
				Response response = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users()
						.delete(userId);
				//logger.info("Deted User Response:- " + response.getEntity());
				userRepo.deleteById(id);
			}
		} catch (Exception e) {
			logger.info("Exception In Delete User impl:- " + e.getMessage(),e);
			userRepo.deleteById(id);
			return "User Not Deleted Properly";
		}
		return "User Deleted Successfully";
	}

	public List<String> getUserListByRole(String roleName) {
		List<String> usersList = new ArrayList<String>();
		try {
			Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
			String realmName = keyCloakClnt.getKeyClkRealm();
			//logger.info("keyclk Instance:- " + keycloak + " Realm Name:- " + realmName);
			RoleResource roleResource = keycloak.realm(realmName).roles().get(roleName);
			Set<UserRepresentation> listOfUsersBasedOnRole = roleResource.getRoleUserMembers();
			for (UserRepresentation user : listOfUsersBasedOnRole) {
				usersList.add(user.getUsername());
			}
		} catch (Exception e) {
			logger.error("Exception occur while in get UserList By Role impl:" + e.getMessage(),e);

		}
		Collections.sort(usersList);
		return usersList;
	}

	public List<String> getRoleNameByUserName(@PathVariable("userName") String userName) {
		String userId = null;
		String roleName = null;
		List<String> roleStringList = new ArrayList<String>();
		Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
		String realmName = keyCloakClnt.getKeyClkRealm();
		List<UserRepresentation> completeUserRepObj = keycloak.realm(realmName).users().search(userName);
		for (UserRepresentation obj : completeUserRepObj) {
			if (userName.equalsIgnoreCase(obj.getUsername())) {
				userId = obj.getId();
			}
		}

		List<RoleRepresentation> roles = keycloak.realm(realmName).users().get(userId).roles().realmLevel()
				.listEffective();
		for (RoleRepresentation role : roles) {
			roleName = role.getName();
			roleStringList.add(roleName);
		}

		name();
		Collections.sort(roleStringList);
		return roleStringList;
	}

	public void name() {

		Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
		String realmName = keyCloakClnt.getKeyClkRealm();
		keycloak.realm(realmName).groups().groups().forEach(groupRep -> logger.info(groupRep.getName()));

		List<GroupRepresentation> groupRepList = keycloak.realm(realmName).groups().groups();
		for (GroupRepresentation gp : groupRepList) {
			//logger.info(gp.getId());
			//logger.info(gp.getName());
			//logger.info(gp.getPath());
			//logger.info("" + keycloak.realm(realmName).getGroupByPath(gp.getPath()).getRealmRoles());
		}
		//logger.info("-----Fetching Group Name:- ");
		GroupRepresentation groupRep = keycloak.realm(realmName).groups().group("d5b12e95-f839-49de-9d6f-87cdf7b6afbe")
				.toRepresentation();
		//logger.info("---------------- " + groupRep.getName());
		keycloak.realm(realmName).getGroupByPath(groupRep.getPath()).getRealmRoles()
		.forEach(role -> logger.info("role:- " + role));
	}

	// modified by kshitiz getRole List API
	public List<String> getRoleList() {
		Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
		String realmName = keyCloakClnt.getKeyClkRealm();
		String roleId = null;
		String roleName = null;

		List<String> roleStringListUsed = new ArrayList<String>();
		RolesResource rolesResource = keycloak.realm(realmName).roles();

		List<RoleRepresentation> rolesList = rolesResource.list();

		for (RoleRepresentation role : rolesList) {

			if (!role.isComposite()) {
				roleId = role.getId();
				roleName = role.getName();
				roleStringListUsed.add(roleId + "," + roleName);
			}

		}
		return roleStringListUsed;
	}

	// Added by Kshitiz Get Group List
	public List<String> getGroupList() {
		Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
		String realmName = keyCloakClnt.getKeyClkRealm();
		String roleId = null;
		String roleName = null;

		List<String> roleStringListUsed = new ArrayList<String>();
		RolesResource rolesResource = keycloak.realm(realmName).roles();

		List<RoleRepresentation> rolesList = rolesResource.list();

		for (RoleRepresentation role : rolesList) {

			if (role.isComposite()) {
				roleId = role.getId();
				roleName = role.getName();
				roleStringListUsed.add(roleId + "," + roleName);
			}
		}
		return roleStringListUsed;
	}

	public List<UserDetailsEntity> getUserDetails(String userName) {
		List<UserDetailsEntity> list = null;
		return list;
	}

	public List<String> getUserLists() {
		String userName = null;
		List<UserRepresentation> usersRepList = new ArrayList<UserRepresentation>();
		usersRepList = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).users().list();

		List<String> userNameLowerCase = new ArrayList<String>();
		for (UserRepresentation userRep : usersRepList) {
			userName = userRep.getUsername();
			userNameLowerCase.add(userName.toLowerCase());
		}
		return userNameLowerCase;
	}

	/**
	 * Creating KeyCloak Groups As Roles
	 * 
	 * @return
	 */
	@Override
	public String checkDupRole(String roleName) {

		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		String dupRoleName = null;
		try {
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(roleName).toRepresentation();
			if (keyClkRoleRep.getName() != null) {
				dupRoleName = keyClkRoleRep.getName();
			}
			//logger.info("Duplicate Role Name :- " + roleName + " equals:- " + keyClkRoleRep.getName());
		} catch (Exception e) {
			//logger.info("In Exception Block in checkDup impl:- " + e.getMessage(),e);
		}
		return dupRoleName;

	}


	// New Method Create Role
	@Override
	public RoleDetailsEntity createRole(RoleDetailsEntity role) {
		RoleDetailsEntity roleDetailsEntity = null;
		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		try {
			keyClkRoleRep.setName(role.getRoleName());
			keyClkRoleRep.setDescription(
					Constant.ROLE_DESCRIPTION.replace("%type%", "role"));
			keyClkRoleRep.setComposite(false);

			// Creating KeyCloak Role
			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles().create(keyClkRoleRep);

			// Checking and Fetching the Same Role Created Properly or Not
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(role.getRoleName()).toRepresentation();

			if (keyClkRoleRep.getId() != null) {
				//logger.info("-----No Role Name Existing:- ");

				role.setId(keyClkRoleRep.getId());
				if(role !=null && role.getRoleName().equals(Constant.READ_ONLY_ROLE)) {
					role.setIsReadOnlyRole("true");
				}else {
					role.setIsReadOnlyRole("false");
				}

				//logger.info("" + role.getId());
				//logger.info("" + role.getRoleName());

				// Saving Data in Db
				roleDetailsEntity = roleDtlsRepo.save(role);
			}

		} catch (Exception e) {
			logger.error("Exception occur while in create Role impl:" + e.getMessage(),e);
		}
		return roleDetailsEntity;
	}

	public String getCreatedId(Response response) {
		URI location = response.getLocation();
		if (!response.getStatusInfo().equals(Status.CREATED)) {
			StatusType statusInfo = response.getStatusInfo();
			throw new WebApplicationException("Create method returned status " + statusInfo.getReasonPhrase()
			+ " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)", response);
		}
		if (location == null) {
			return null;
		}
		String path = location.getPath();
		//logger.info("Response Path:- " + path);
		return path.substring(path.lastIndexOf('/') + 1);
	}

	@Override
	public List<RoleDetailsEntity> fetchRoleList() {
		return roleDtlsRepo.findAll();
	}

	@Override
	public List<RoleSummary> fetchOnlyRole() {
		List<RoleSummary> roleDetails = null;
		try {
			roleDetails = roleDtlsRepo.findOnlyRole();
		} catch (Exception e) {
			logger.error("Exception occur while in fetch OnlyRole impl:" + e.getMessage(),e);

		}
		return roleDetails;
	}

	// Update KeyCloak Groups As Role
//	public RoleDetailsEntity updateGroup1(String roleId, RoleDetailsEntity roleDtls) {
//		RoleDetailsEntity updatedRole = null;
//		try {
//			GroupRepresentation updateGrpRep = new GroupRepresentation();
//			updateGrpRep.setName(roleDtls.getRoleName());
//
//			//logger.info("Updating in KeyCloak Group Id:- " + roleDtls.getId());
//
//			logger.info("Fetching KeyCloak Group Rep :- "
//					+ keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).groups()
//					.group(roleDtls.getId()).toRepresentation().getName());
//
//			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).groups().group(roleDtls.getId())
//			.update(updateGrpRep);
//
//			//logger.info("Checking and Updating in DB" + roleId);
//			updatedRole = roleDtlsRepo.findById(roleId).map(roleDtlsE -> {
//				roleDtlsE.setId(roleId);
//				return roleDtlsRepo.save(roleDtls);
//			}).orElseThrow(() -> new ResourceNotFoundException("Role Id " + roleId + " not found"));
//		} catch (ResourceNotFoundException e) {
//			logger.error("Exception occur while in updateGroup1" + e.getMessage(),e);
//
//		} catch (Exception e) {
//			logger.error("Exception occur while in updateGroup1 impl: " + e.getMessage(),e);
//
//		}
//		return updatedRole;
//	}

	// New Method Update Role
	@Override
	public RoleDetailsEntity updateRole(String roleId, RoleDetailsEntity roleDtls) {
		RoleDetailsEntity updatedRole = null;
		try {
			//logger.info("Checking and Updating Role Details in DB:- " + roleId);
			if(roleDtls !=null && roleDtls.getRoleName().equals(Constant.READ_ONLY_ROLE)) {
				roleDtls.setIsReadOnlyRole("true");
			}else {
				roleDtls.setIsReadOnlyRole("false");
			}
			updatedRole = roleDtlsRepo.findById(roleId).map(roleDtlsE -> {
				roleDtlsE.setId(roleId);
				return roleDtlsRepo.save(roleDtls);
			}).orElseThrow(() -> new ResourceNotFoundException("Role Id " + roleId + " not found"));

		} catch (ResourceNotFoundException e) {
			logger.error("Exception occur while in updateRole impl: " + e.getMessage(),e);
		}
		return updatedRole;
	}

	/**
	 * Creating KeyCloak Roles As Group
	 * 
	 * @throws ResourceNotFoundException
	 */
	@Override
	@ExceptionHandler(ResourceNotFoundException.class)
	public String checkDupGroup(String groupName) throws ResourceNotFoundException {
		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		String dupGroupName = null;
		try {
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(groupName).toRepresentation();
			if (keyClkRoleRep.getName() != null) {
				dupGroupName = keyClkRoleRep.getName();
			}
			//logger.info("Duplicate group Name :- " + groupName + " equals:- " + keyClkRoleRep.getName());
		} catch (Exception e) {
			//logger.info("In Exception Block in checkDupGroup impl:- " + e.getMessage(),e);
		}
		return dupGroupName;
	}

//	@Override
//	public GroupDetailsEntity updateRole1(String groupId, GroupDetailsEntity groupDetails) {
//		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
//		GroupDetailsEntity returnGrpDtls = null;
//		try {
//
//			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
//					.get(groupDetails.getGroupName()).toRepresentation();
//			if (keyClkRoleRep.getId() != null) {
//				for (RoleDetailsEntity grpRoleE : groupDetails.getGrpRoleList()) {
//
//					//logger.info("Role id :- " + grpRoleE.getId());
//					GroupRepresentation groupRep = keyCloakClnt.getkeycloakInstance()
//							.realm(keyCloakClnt.getKeyClkRealm()).groups().group(grpRoleE.getId()).toRepresentation();
//
//					//logger.info("----------------> " + groupRep.getName());
//
//					List<RoleRepresentation> roles = new LinkedList<>();
//					roles.add(keyClkRoleRep);
//
//					keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).groups()
//					.group(groupRep.getId()).roles().realmLevel().add(roles);
//
//					// Fetching Realm Roles based on Group Path
//					keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
//					.getGroupByPath(groupRep.getPath()).getRealmRoles()
//					.forEach(role -> logger.info("role:- " + role));
//				}
//
//				//logger.info("" + groupDetails.getId());
//				//logger.info("" + groupDetails.getGroupName());
//
//				groupDetails.getGrpRoleList()
//				.forEach(action -> logger.info("Intial RoleName :- " + action.getRoleName()));
//
//				returnGrpDtls = groupDtlsRepo.findById(groupId).map(groupDtlsE -> {
//					groupDtlsE.setId(groupId);
//					return groupDtlsRepo.save(groupDetails);
//				}).orElseThrow(() -> new ResourceNotFoundException("groupId" + groupId + " not found"));
//
//				returnGrpDtls.getGrpRoleList().forEach(action -> logger.info("RoleName+ " + action.getRoleName()));
//
//			}
//
//		} catch (ResourceNotFoundException e) {
//			logger.error("Exception occur while in updateRole1 impl: " + e.getMessage(),e);
//		}
//		return returnGrpDtls;
//	}

	// New Method Create Group as Role
	@Override
	public GroupDetailsEntity createGroupAsRole(GroupDetailsEntity group) {
		GroupDetailsEntity groupDetailsEntity = null;
		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		try {

			keyClkRoleRep.setName(group.getGroupName());
			keyClkRoleRep
			.setDescription(Constant.ROLE_DESCRIPTION.replace("%type%", "group").replace("%mdule%", "N/A"));
			// Creating of KeyCloak Normal Role
			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles().create(keyClkRoleRep);

			// Checking and Fetching the Same Role Created Properly or Not
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(group.getGroupName()).toRepresentation();
			//logger.info("(keyClkRoleRep.getId()" + keyClkRoleRep.getId());

			List<RoleRepresentation> roleRepListToAdd = new LinkedList<RoleRepresentation>();

			for (RoleDetailsEntity grpRoleE : group.getGrpRoleList()) {
				RoleRepresentation roleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
						.roles().get(grpRoleE.getRoleName()).toRepresentation();

				//logger.info("Hash Code of the Object:- " + roleRep.hashCode());
				//logger.info("KeyCloak Role Name:- " + roleRep.getName());
				roleRepListToAdd.add(roleRep);
				//logger.info("roleRepListToAdd" + roleRepListToAdd);
				if (!roleRep.isComposite()) {
					keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(keyClkRoleRep.getName()).addComposites(roleRepListToAdd);
				}
				logger.info("------------associated groups before-------------"
						+ keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
						.get(keyClkRoleRep.getName()).getRealmRoleComposites());

			}

			if (keyClkRoleRep.getId() != null) {
				//logger.info("-----No Role Name Existing:- ");
				group.setId(keyClkRoleRep.getId());
				//logger.info("" + group.getId());
				//logger.info("" + group.getGroupName());

				// Saving Data in Db

				if(group !=null && group.getGroupName().equals(Constant.READ_ONLY_GROUP)) {
					group.setIsReadOnlyGroup("true");
				}else {
					group.setIsReadOnlyGroup("false");
				}
				groupDetailsEntity = groupDtlsRepo.save(group);
			}

		} catch (Exception e) {
			logger.error("Exception occur while in createGroupAsRole impl: " + e.getMessage(),e);
		}
		return groupDetailsEntity;
	}

	// New Method Update Group as Role
	@Override
	public GroupDetailsEntity updateGroupAsRole(String groupId, GroupDetailsEntity groupDtls) {
		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		GroupDetailsEntity updatedRole = null;
		List<RoleRepresentation> roleRepListToAdd = new LinkedList<RoleRepresentation>();

		// deleting associated roles
		List<RoleRepresentation> roleRepListTodelete = new LinkedList<RoleRepresentation>();
		Set<RoleRepresentation> compositeRoles = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
				.roles().get(groupDtls.getGroupName()).getRealmRoleComposites();
		//logger.info("Set<RoleRepresentation> compositeRoles : " + compositeRoles);
		for (RoleRepresentation s : compositeRoles) {
			roleRepListTodelete.add(keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(s.getName()).toRepresentation());
		}
		keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles().get(groupDtls.getGroupName())
		.deleteComposites(roleRepListTodelete);

		// Updating
		keyClkRoleRep.setName(groupDtls.getGroupName());
		keyClkRoleRep.setId(groupId);
		keyClkRoleRep.setDescription(Constant.ROLE_DESCRIPTION.replace("%type%", "group").replace("%mdule%", "N/A"));

		try {
			for (RoleDetailsEntity grpRoleE : groupDtls.getGrpRoleList()) {

				RoleRepresentation roleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
						.roles().get(grpRoleE.getRoleName()).toRepresentation();

				//logger.info("Hash Code of the Object:- " + roleRep.hashCode());
				//logger.info("KeyCloak Role Name:- " + roleRep.getName());
				roleRepListToAdd.add(roleRep);
				//logger.info("roleRepListToAdd" + roleRepListToAdd);
				keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
				.get(groupDtls.getGroupName()).addComposites(roleRepListToAdd);

			}

			keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
			.get(groupDtls.getGroupName()).update(keyClkRoleRep);


			if(groupDtls !=null && groupDtls.getGroupName().equals(Constant.READ_ONLY_GROUP)) {
				groupDtls.setIsReadOnlyGroup("true");
			}else {
				groupDtls.setIsReadOnlyGroup("false");
			}


			//logger.info("Checking and Updating in DB" + groupId);
			updatedRole = groupDtlsRepo.findById(groupId).map(roleDtlsE -> {
				roleDtlsE.setId(groupId);
				return groupDtlsRepo.save(groupDtls);
			}).orElseThrow(() -> new ResourceNotFoundException("Group Id " + groupId + " not found"));

		} catch (ResourceNotFoundException e) {
			logger.error("Exception occur while in createGroupAsRole impl: " + e.getMessage(),e);
		}
		return updatedRole;
	}

	@Override
	public GroupDetailsEntity updateGroupAsDefault(String groupId) {
		GroupDetailsEntity groupDetails = null;
		String setdefaultGroupToYes = "YES";
	//	String module = "IM";
		try {
			GroupSummary existingGrp = groupDtlsRepo
					.findDistinctByDefaultGroup(setdefaultGroupToYes);

			if (existingGrp != null) {

				logger.info("Existing Default Group Id Updating to NO:- " + existingGrp.getId() + " Group Name:- "
						+ existingGrp.getGroupName());
				groupDtlsRepo.findById(existingGrp.getId()).ifPresent(existingGroupDtls -> {
					existingGroupDtls.setDefaultGroup("NO");
					groupDtlsRepo.save(existingGroupDtls);
				});
			}
			//logger.info("Group Id Updating to YES :- " + groupId);
			groupDetails = groupDtlsRepo.findById(groupId).map(groupDtls -> {
				groupDtls.setDefaultGroup(setdefaultGroupToYes);
				return groupDtlsRepo.save(groupDtls);
			}).orElseThrow(() -> new ResourceNotFoundException("Group Id " + groupId + " not found"));

		} catch (Exception e) {
			logger.error("Exception occur while in updateGroupAsDefault impl: " + e.getMessage(),e);
		}
		return groupDetails;
	}
	
	public GroupDetailsEntity updateGroupModuleAsDefault(String groupId, String module) {
		GroupDetailsEntity groupDetails = null;
		try {

			List<String> moduleList = Stream.of(module.split(",", -1)).collect(Collectors.toList());
			for (String mod : moduleList) {
				GroupSummary existingGrp = groupDtlsRepo.findDistinctByDefaultModuleGroupContaining(mod.toUpperCase());
				if (existingGrp != null) {
					List<String> existingDefaultGrpList = Stream.of(existingGrp.getDefaultModuleGroup().split(",", -1))
							.collect(Collectors.toList());
					existingDefaultGrpList.removeIf(m ->  m.equalsIgnoreCase(mod));

					if (!existingDefaultGrpList.isEmpty()) {
						groupDtlsRepo.findById(existingGrp.getId()).ifPresent(existingGroupDtls -> {
							existingGroupDtls.setDefaultModuleGroup(existingDefaultGrpList.stream().collect(Collectors.joining(",")));
							groupDtlsRepo.save(existingGroupDtls);
						});
					} else {
						groupDtlsRepo.findById(existingGrp.getId()).ifPresent(existingGroupDtls -> {
							existingGroupDtls.setDefaultModuleGroup("NO");
							groupDtlsRepo.save(existingGroupDtls);
						});
					}
				}
			}
			groupDetails = groupDtlsRepo.findById(groupId).map(groupDtls -> {
				groupDtls.setDefaultModuleGroup(module.toUpperCase());
				return groupDtlsRepo.save(groupDtls);
			}).orElseThrow(() -> new ResourceNotFoundException("Group Id " + groupId + " not found"));

		} catch (Exception e) {
			
			logger.error("Exception occur while in updateGroupAsDefault By Module impl: " + e.getMessage(), e);
		}
		return groupDetails;
	}


	@Override
	public List<GroupDetailsEntity> fetchGroupList() {
		return groupDtlsRepo.findAll();
	}

	@Override
	public List<GroupSummary> fetchOnlyGroup() {
		return groupDtlsRepo.findOnlyGroup();
	}

	public List<GroupSummary> fetchOnlyGroup(String module) {
		List<GroupSummary> allgroupListExceptReadOnly = new ArrayList<GroupSummary>();
		try {
			allgroupListExceptReadOnly = groupDtlsRepo.findOnlyGroup(module);
			allgroupListExceptReadOnly
			.removeIf(group -> group.getGroupName().equalsIgnoreCase(Constant.READ_ONLY_GROUP));
		} catch (Exception e) {
			logger.error("Exception occured in fetchOnlyGroup by Module: " + e.getMessage(), e);
		}
		return allgroupListExceptReadOnly;
	}

	/* Deleting KeyCloak Roles as Group */
	@Override
	public String deleteGroup(String groupId, String groupName) {

		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		try {
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(groupName).toRepresentation();
			if (keyClkRoleRep.getId() != null) {
				keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles().deleteRole(groupName);
				groupDtlsRepo.deleteByGroupID(groupId);
				groupDtlsRepo.deleteById(groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured in deleteGroup: " + e.getMessage(), e);  
			groupDtlsRepo.deleteByGroupID(groupId);
			groupDtlsRepo.deleteById(groupId);
			return "Problem in deleting group";
		}

		return "Group Succesffully Deleted";

	}

	@Override
	public String createClientRole(RoleDetailsEntity role) {
		RoleRepresentation roleRep = new RoleRepresentation();
		roleRep.setName(role.getRoleName());

		//logger.info("" + roleRep.getName());

		ClientRepresentation clientRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm())
				.clients().findByClientId(keyCloakClnt.getKeyClkClientId()).stream()
				.filter(clientRep1 -> clientRep1.getClientId().equalsIgnoreCase(keyCloakClnt.getKeyClkClientId()))
				.findAny().get();

		//logger.info("Id:- " + clientRep.getId() + " Client Id:- " + clientRep.getClientId());

		logger.info("" + keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).clients()
				.findByClientId(keyCloakClnt.getKeyClkClientId()).listIterator().next().getClientId());

		keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).clients().get(clientRep.getId()).roles()
		.create(roleRep);

		return roleRep.getName();
	}

	@Override
	public String getUserTheme(String userName) {
		String userTheme = "";
		try {
			if (userName != null && !"".equals(userName)) {
				userTheme = userRepo.findUserThemeByEmail(userName);
				//logger.info("User theme of the logged in User:- " + userTheme);
			}
		} catch (Exception e) {
			logger.error("Exception occur while in get User Theme impl: " + e.getMessage(),e);
		}
		return userTheme;
	}



	@Override
	public Set<GroupSummary> fetchGroupListByEmail(String userName) {
		Set<GroupSummary> groupDtls = groupDtlsRepo.findGroupListByUser_Email(userName);

		String groupListAsString = groupDtls.stream().filter(group -> group.getGroupName() != null)
				.map(GroupSummary::getGroupName).collect(Collectors.joining(","));
		List<String> list = Arrays.asList(groupListAsString.split(","));
		return groupDtls;
	}

	@Override
	public Set<UserDetailsDTO> fetchUserByMultipleGroup(List<GroupDetailsEntity> jsonPayLoad) {

		Set<UserDetailsDTO> userDetailsDTO = new HashSet<UserDetailsDTO>();
		for (GroupDetailsEntity groupDetails : jsonPayLoad) {
			Set<UserDetailsSummary> groupSummary = userRepo.findByMultipleGroupId(groupDetails.getId());
			for (UserDetailsSummary UserDtls : groupSummary) {
				UserDetailsDTO userr = new UserDetailsDTO();
				userr.setEmail(UserDtls.getEmail());
				userr.setFullName(UserDtls.getFullName());
				userr.setId(UserDtls.getId());
				userr.setUserId(UserDtls.getUserId());
				userDetailsDTO.add(userr);
			}
		}
		return userDetailsDTO;
	}

	@Override
	public List<GroupSummary> fetchGroupListByEmailAndModule(String userName, String module) {

		List<GroupSummary> allgroupList = new ArrayList<GroupSummary>();
		try {
			allgroupList = groupDtlsRepo.fetchGroupListByEmailAndModule(userName, module);
			if (allgroupList.get(0).getGroupName().equalsIgnoreCase(Constant.READ_ONLY_GROUP)) {
				allgroupList = groupDtlsRepo.findOnlyGroup(module);
				allgroupList.removeIf(group -> group.getGroupName().equalsIgnoreCase(Constant.READ_ONLY_GROUP));
			}

		} catch (Exception e) {
			logger.error("Exception occured in fetchGroupListByEmailAndModule impl: " + e.getMessage(), e);
		}
		return allgroupList;
	}

	// Creating Rules to Auto Close the Resolved Tickets
	@Override
	public AutoClosureDetailsEntity createAutoCloseRule(AutoClosureDetailsEntity autoCloseDtls) {
		return autoClosureRepo.save(autoCloseDtls);
	}

	// Updating Rules to Auto Close the Resolved Tickets
	@Override
	public AutoClosureDetailsEntity updateAutoCloseRule(Long autoCloseRuleId,
			@Valid AutoClosureDetailsEntity autoCloseDtls) {
		try {
			return autoClosureRepo.findById(autoCloseRuleId).map(roleDtlsE -> {
				roleDtlsE.setId(autoCloseRuleId);
				return autoClosureRepo.save(autoCloseDtls);
			}).orElseThrow(() -> new ResourceNotFoundException("Auto Clsoe Rule Id " + autoCloseRuleId + " not found"));
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			logger.error("Exception occured in updateAutoCloseRule impl: " + e.getMessage(), e);  

		}
		return autoCloseDtls;
	}

	// Fetching All Auto Close Rules Based on Module
	@Override
	public List<AutoClosureDetailsEntity> fetchAllAutoCloseRules(String module) {
		return autoClosureRepo.findByModule(module);
	}

	// Fetching Auto Close Rule by CatId, PriorityId and Module
	@Override
	public AutoClosureDetailsEntity fetchAutoCloseRule(Long catId, String priorityName, String module) {
		return autoClosureRepo.findByCatDetails_IdAndPriorityDetails_NameAndModule(catId, priorityName, module);
	}

	// Fetching Auto Close Time by CatId, PriorityId and Module
	@Override
	public String fetchAutoCloseTime(Long catId, String priorityName, String module) {
		String autoCloseTime = null;
		AutoClosureDetailsEntity autoCloseDtls = null;
		try {
			autoCloseDtls = fetchAutoCloseRule(catId, priorityName, module);
			if (autoCloseDtls != null) {
				autoCloseTime = autoCloseDtls.getAutoCloseTime();
				//logger.info("Auto Close Time:- " + autoCloseTime);
			} else {
				autoCloseTime = autoClosureRepo.findAutoClosureTimeByModule(module);
				//logger.info("Auto Close Default Time:- " + autoCloseTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured in fetchAutoCloseTime impl: " + e.getMessage(), e);  

		}
		return autoCloseTime;
	}

	@Override
	public void deleteAutoCloseRule(Long autoCloseRuleId) {
		autoClosureRepo.deleteById(autoCloseRuleId);
	}

	public boolean fetchPagesByAuthToken(String authToken, String pageName) {

		List<String> pageSummary = pageAccessRepo.findPageNameByAuthToken(authToken).stream()
				.map(PageSummary::getPageName).collect(Collectors.toList());

		//logger.info("pageAccessRepo.findPageNameByUserName(userName)------------------" + pageSummary);

		if (pageSummary.contains(pageName)) {
			//logger.info("===============True====================");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String deleteRole(String roleId, String roleName) {

		RoleRepresentation keyClkRoleRep = new RoleRepresentation();
		try {
			keyClkRoleRep = keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles()
					.get(roleName).toRepresentation();
			if (keyClkRoleRep.getId() != null) {
				keyCloakClnt.getkeycloakInstance().realm(keyCloakClnt.getKeyClkRealm()).roles().deleteRole(roleName);
				roleDtlsRepo.deleteByRole_Page_Access(roleId);
				roleDtlsRepo.deleteByGroup_Role(roleId);
				roleDtlsRepo.deleteById(roleId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			roleDtlsRepo.deleteByRole_Page_Access(roleId);
			roleDtlsRepo.deleteByGroup_Role(roleId);
			roleDtlsRepo.deleteById(roleId);
			logger.error("Exception occured in deleteRole impl: " + e.getMessage(), e);  
			return "Problem in deleting role";
		}

		return "Role Succesffully Deleted";

	}

	@Override
	public List<HashMap<String, Object>> fetchAllUserNameAndGroupDetails() {

		List<UserDetailsEntity> list = userRepo.findAll();

		List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String,Object>>();


		for(UserDetailsEntity lUserDetailsEntity :list)
		{
			if (StringUtil.isNotNullNotEmpty(lUserDetailsEntity.getFullName())
					&& StringUtil.isNotNullNotEmpty(lUserDetailsEntity.getEmail())
					&& StringUtil.isNotNullNotEmpty(lUserDetailsEntity.getContactNo())) {


				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", lUserDetailsEntity.getFullName());
				map.put("email", lUserDetailsEntity.getEmail());
				map.put("isGroup", false);
				map.put("mobileNumber", lUserDetailsEntity.getContactNo());

				returnList.add(map);
			}
		}

		List<GroupDetailsEntity> groupList = groupDtlsRepo.findAll();

		for(GroupDetailsEntity lGroupDetailsEntity :groupList)
		{

			if(StringUtil.isNotNullNotEmpty(lGroupDetailsEntity.getGroupName()))
			{

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", lGroupDetailsEntity.getGroupName());
				map.put("email", lGroupDetailsEntity.getEmail());
				map.put("isGroup", true);
				map.put("mobileNumber", "N/A");
				List<HashMap<String, Object>> internalUSersList = new ArrayList<HashMap<String, Object>>();

				List<UserDetailsSummary> usersListByGroup = userRepo.findUserByGroupList_IdAndGroupList_GroupName(
						lGroupDetailsEntity.getId(), lGroupDetailsEntity.getGroupName());
				for (UserDetailsSummary lUserDetailsSummary : usersListByGroup) {

					if (StringUtil.isNotNullNotEmpty(lUserDetailsSummary.getFullName())
							&& StringUtil.isNotNullNotEmpty(lUserDetailsSummary.getEmail())
							&& StringUtil.isNotNullNotEmpty(lUserDetailsSummary.getContactNo())) {

						HashMap<String, Object> map1 = new HashMap<String, Object>();
						map1.put("name", lUserDetailsSummary.getFullName());
						map1.put("email", lUserDetailsSummary.getEmail());
						map1.put("isGroup", false);
						map1.put("mobileNumber", lUserDetailsSummary.getContactNo());
						internalUSersList.add(map1);
					}
				}
				map.put("groupMembersDetails", internalUSersList);

				returnList.add(map);
			}
		}

		return returnList;
	}

	@Override
	public List<HashMap<String, String>> fetchFullNameOnUserId(String payload) {

		List<HashMap<String, String>> finalResult = new ArrayList<HashMap<String, String>>();
		try {
			if (StringUtil.isNotNullNotEmpty(payload)) {

				HashMap<String, String> filter = new HashMap<String, String>();
				filter = JSONUtil.jsonpayloadMapToHashMap(payload);
				String userId = filter.get("emailId");
				String user[] = userId.split(",");
				List<String> userID = Arrays.asList(user);
				for (String luserId : userID) {
					HashMap<String, String> result = new HashMap<String, String>();
					String fullName = userRepo.findFullNameByEmail(luserId);
					result.put(luserId, fullName);
					finalResult.add(result);
				}

			}
		} catch (Exception e) {

			logger.error("Exception occured in calling fullName impl: " + e.getMessage(), e);
		}
		return finalResult;
	}
	
	@Override
	public String getUserNameFromAuth(String auth) {
		String username = userRepo.findByAuthToken(auth).getEmail();
		return username;
		
	}
	
	@Override
	public String getFullUserNameFromAuth(String auth) {
		if(StringUtil.isNotNullNotEmpty(auth))
		{
			String username = userRepo.findByAuthToken(auth).getEmail();
			if(StringUtil.isNotNullNotEmpty(username))
			{
				return userRepo.findFullNameByEmail(username);
			}
		}
		
		return null;
		
	}
	
	@Override
	public List<UserDetailsEntity> syncUsersWithPostgres(){
		Keycloak keycloak = keyCloakClnt.getkeycloakInstance();
		String realmName = keyCloakClnt.getKeyClkRealm();
		List<UserRepresentation> completeUserRepObj = keycloak.realm(realmName).users().list();	
		List<UserDetailsEntity> userListFromDB = userRepo.findAll();
		
		for(UserDetailsEntity userlist :userListFromDB) {
			
			for(UserRepresentation keycloakUser: completeUserRepObj) {
				if(userlist.getEmail().equals(keycloakUser.getUsername())) {
					userlist.setUserId(keycloakUser.getId());
					break;
				}
			}
		}
		userRepo.saveAll(userListFromDB);
		
		
		
		return userListFromDB;
	}
}
