package com.tcts.foresight.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.GroupDetailsEntity;

public interface GroupDetailsRepository
		extends CrudRepository<GroupDetailsEntity, String>, JpaRepository<GroupDetailsEntity, String> {

	@Query("select group from GroupDetailsEntity group")
	List<GroupSummary> findOnlyGroup();

	@Query(nativeQuery = true, value = "select gd.name as groupname, gd.id, gd.email as email, gd.default_group as defaultGroup, \r\n"
			+ "gd.is_read_only_group as isReadOnlyGroup, gd.default_module_group as defaultModuleGroup \r\n"
			+ "from pagenames_details pd\r\n" + "join role_page_access rpa on rpa.page_id = pd.id\r\n"
			+ "join role_details rd on rd.id = rpa.role_id\r\n" + "join group_role gr on rd.id = gr.role_id \r\n"
			+ "join group_details gd on gd.id = gr.group_id \r\n" + "where pd.module = :module \r\n"
			+ "group by groupname,gd.id,email ")
	List<GroupSummary> findOnlyGroup(@Param("module") String module);

	@Query(nativeQuery = true, value = "select gd.id as id, gd.name as groupName, gd.email as email from group_details gd  left join user_groups ug on ug.group_id = gd.id left join user_details ud on ug.user_id = ud.id where ud.email =:userName")
	Set<GroupSummary> findGroupListByUser_Email(@Param("userName") String userName);

	@Query(nativeQuery = true, value = "DELETE FROM user_groups ug WHERE ug.group_id = :groupId")
	@Modifying
	void deleteByGroupID(@Param("groupId") String groupId);

//	GroupSummary findDistinctByDefaultGroupAndGrpRoleList_Module(String defaultGroup, String module);

	GroupSummary findDistinctByDefaultGroup(String defaultGroup);

	// @Modifying(clearAutomatically = true)
	@Query("UPDATE GroupDetailsEntity grp SET grp.defaultGroup = :defalutGrp WHERE grp.id = :groupId")
	GroupDetailsEntity updateGroupSetDefault(@Param("groupId") String groupId, @Param("defalutGrp") String defalutGrp);

	@Query(nativeQuery = true, value = "select  Distinct gd.name as groupname ,gd.id, gd.email as email , gd.default_group as defaultgroup,\r\n"
			+ "gd.is_read_only_group as isReadOnlyGroup,gd.default_module_group as defaultModuleGroup \r\n"
			+ "from user_details ud \r\n" + "join user_groups ug on ud.id = ug.user_id\r\n"
			+ "join group_details gd on gd.id = ug.group_id \r\n" + "join group_role gr on gd.id = gr.group_id\r\n"
			+ "join role_details rd on rd.id = gr.role_id \r\n"
			+ "join role_page_access rpa on rpa.role_id = rd.id \r\n"
			+ "join pagenames_details pd on pd.id = rpa.page_id \r\n"
			+ "where ud.email = :email  and pd.module= :module")
	List<GroupSummary> fetchGroupListByEmailAndModule(@Param("email") String email, @Param("module") String module);

	@Query(value = "select name from group_details where id=:id", nativeQuery = true)
	String findNameById(@Param("id") String id);

	@Query(nativeQuery = true, value = "select name from group_details where email = :userName")
	String findGroupNameByEmail(@Param("userName") String userName);

	GroupSummary findDistinctByDefaultModuleGroup(String defaultModuleGroup);

	GroupSummary findDistinctByDefaultModuleGroupContaining(String defaultModuleGroup);

	@Query(value = "select id from group_details where name=:name", nativeQuery = true)
	String findIDByGroupName(@Param("name") String name);

	@Query(nativeQuery = true, value = "select  Distinct gd.name as groupname ,gd.id, gd.email as email , gd.default_group as defaultgroup,\r\n"
			+ "gd.is_read_only_group as isReadOnlyGroup,gd.default_module_group as defaultModuleGroup from group_details gd\r\n"
			+ "join user_groups ur on ur.group_id = gd.id\r\n" + "join user_details ud on ur.user_id = ud.id\r\n"
			+ "where ud.authtoken =:authToken")
	List<GroupSummary> findGroupDetailByAuthToken(@Param("authToken") String authToken);

//	@Query(nativeQuery = true, value = "select  Distinct gd.name as groupname ,gd.id, gd.email as email , gd.default_group as defaultgroup from group_details as gd")
//	List<GroupSummary> findAllGroups();
//	

	@Query(value = "select  Distinct pd.module from group_details gd\r\n"
			+ "join group_role gr on gd.id = gr.group_id\r\n" + "join role_details rd on rd.id = gr.role_id\r\n"
			+ "join role_page_access rpa on rpa.role_id = rd.id\r\n"
			+ "join pagenames_details pd on pd.id = rpa.page_id\r\n" + "where gd.id = :groupId", nativeQuery = true)
	List<String> findModulebyGroupname(@Param("groupId") String groupId);

	@Query(value = "select email from user_details \r\n"
			+ "u join user_groups g On u.id = g.user_id where g.group_id = :groupId", nativeQuery = true)
	List<String> findEmailByGroupId(@Param("groupId") String groupId);
	
	@Query(value = "select email from user_details \r\n"
			+ "u join user_groups g On u.id = g.user_id where g.name = :groupName", nativeQuery = true)
	List<String> findEmailByGroupName(@Param("groupName") String groupName);

}
