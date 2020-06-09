package com.tcts.foresight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.keycloak.representations.idm.UserRepresentation;

import com.tcts.foresight.entity.AutoClosureDetailsEntity;
import com.tcts.foresight.entity.GroupDetailsEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.PageNamesDetailsEntity;
import com.tcts.foresight.entity.RoleDetailsEntity;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.pojo.UserDetailsDTO;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.RoleSummary;
import com.tcts.foresight.repository.UserDetailsSummary;

public interface UserManagementService {

	public List<PageNamesDetailsEntity> getPageAccessList(String module);

	public List<PageNamesDetailsEntity> getPageAccessList();
	
	public List<PageNamesDetailsEntity> getPageAccessbyRoles(String module, List<String> roles);

	public UserDetailsEntity createUser(UserDetailsEntity user);

	public UserDetailsEntity updateUser(Long userId, UserDetailsEntity userReq) throws ResourceNotFoundException;

	public UserDetailsEntity updateTheme(String userTheme, String userName) throws ResourceNotFoundException;

	public List<UserDetailsEntity> getAllUser();

	public String fetchUserName(String userName);

	public UserDetailsEntity fetchUserDataByEmail(String userName);

	public List<UserDetailsSummary> fetchUserByGroup(String groupId, String groupName);

	public UserDetailsDTO getRolesByUserName(String userName);

	public String deleteUser(Long id, String userId);

	public List<String> getUserListByRole(String roleName);

	public List<String> getRoleNameByUserName(String userName);

	public List<String> getRoleList();

	public List<String> getGroupList();

	public List<UserDetailsEntity> getUserDetails(String userName);

	public List<String> getUserLists();

	// Creating KeyCloak Groups As Roles :- CheckDupGroup
	public String checkDupRole(String roleName);

	// Creating KeyCloak Roles as Groups :- checkDupRole
	public String checkDupGroup(String groupName) throws ResourceNotFoundException;


	// Changing to Method updateGroup to updateGroup1 Madhav
	//public RoleDetailsEntity updateGroup1(String roleId, RoleDetailsEntity roleDtls);


	// Changing to Method updateRole to updateRole1 Madhav
//	public GroupDetailsEntity updateRole1(String groupId, GroupDetailsEntity groupDtls);

	public String createClientRole(RoleDetailsEntity role);

	public List<RoleDetailsEntity> fetchRoleList();

	public List<RoleSummary> fetchOnlyRole();

	public List<GroupDetailsEntity> fetchGroupList();

	public List<GroupSummary> fetchOnlyGroup();

	public List<GroupSummary> fetchOnlyGroup(String module);

	public String getUserTheme(String lowerCase);

	public String deleteGroup(String groupId, String groupName);

	public List<UserDetailsEntity> bulkUserCreation(List<UserDetailsEntity> bulkUserList);

	// New Method Create Role
	public RoleDetailsEntity createRole(RoleDetailsEntity role);

	// New Method Update Role
	public RoleDetailsEntity updateRole(String roleId, RoleDetailsEntity roleDtls);

	// New Method Create Group as Role
	public GroupDetailsEntity createGroupAsRole(GroupDetailsEntity group);

	// New Method Update Group as Role
	public GroupDetailsEntity updateGroupAsRole(String groupId, GroupDetailsEntity groupDtls);

	// Update Group as Default
	public GroupDetailsEntity updateGroupAsDefault(String groupId);

	// New Imple
	public Set<GroupSummary> fetchGroupListByEmail(String userName);

	// Call Multiple users from multiple group
	public Set<UserDetailsDTO> fetchUserByMultipleGroup(List<GroupDetailsEntity> jsonPayLoad);

	// Call GroupList By username and module
	public List<GroupSummary> fetchGroupListByEmailAndModule(String userName, String module);

	// Creating Rules to Auto Close the Resolved Tickets
	public AutoClosureDetailsEntity createAutoCloseRule(AutoClosureDetailsEntity autoCloseDtls);

	// Updating Rules to Auto Close the Resolved Tickets
	public AutoClosureDetailsEntity updateAutoCloseRule(Long autoCloseRuleId,
			@Valid AutoClosureDetailsEntity autoCloseDtls);	

	// Fetching All Auto Close Rules Based on Module
	public List<AutoClosureDetailsEntity> fetchAllAutoCloseRules(String module);

	// Fetching Auto Close Rule by CatId, PriorityId and Module
	public AutoClosureDetailsEntity fetchAutoCloseRule(Long catId, String priorityName, String module);
	
	// Fetching Auto Close Time by CatId, PriorityId and Module
	public String fetchAutoCloseTime(Long catId, String priorityName, String module);

	// Delete Auto Close Rule 
	public void deleteAutoCloseRule(Long autoCloseRuleId);

	// Fetching Pages Based on Authoken Saved in Database
	public boolean fetchPagesByAuthToken(String authToken, String pageName);

	HashMap<String, String> resetUserPasswd(@Valid UserDetailsEntity userReq);

	public String deleteRole(String roleId, String roleName);

	public List<HashMap<String, Object>> fetchAllUserNameAndGroupDetails();

	public List<HashMap<String, String>> fetchFullNameOnUserId(String payload);

	public GroupDetailsEntity updateGroupModuleAsDefault(String groupId, String module);
	
	public String getUserNameFromAuth(String auth);

	String getFullUserNameFromAuth(String auth);
	
	public List<UserDetailsEntity> syncUsersWithPostgres();
	
	}
