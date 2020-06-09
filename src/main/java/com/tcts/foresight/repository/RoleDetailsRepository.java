package com.tcts.foresight.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.PageNamesDetailsEntity;
import com.tcts.foresight.entity.RoleDetailsEntity;

public interface RoleDetailsRepository
		extends CrudRepository<RoleDetailsEntity, String>, JpaRepository<RoleDetailsEntity, String> {

//	@Query("select id as id, roleName as roleName, module as module, isReadOnlyRole as isReadOnlyRole from RoleDetailsEntity ")
//	List<RoleSummary> findOnlyRole();
	
	
	@Query("select id as id, roleName as roleName, isReadOnlyRole as isReadOnlyRole from RoleDetailsEntity ")
	List<RoleSummary> findOnlyRole();

//	@Query("select id as id, roleName as roleName, module as module, isReadOnlyRole as isReadOnlyRole from RoleDetailsEntity where roleName=:roleName")
//	RoleSummary findOnlyRole(@Param("roleName") String roleName);
	
	@Query("select id as id, roleName as roleName, isReadOnlyRole as isReadOnlyRole from RoleDetailsEntity where roleName=:roleName")
	RoleSummary findOnlyRole(@Param("roleName") String roleName);

//	@Query("select distinct role.pageAccessControl as pageAccessControl from RoleDetailsEntity role JOIN role.pageAccessControl page where role.roleName in (:roles) and page.module =:module")
//	List<PageNamesDetailsEntity> findPageAccessByRoleName(@Param("module") String module,@Param("roles") List<String> roles);

	@Query(nativeQuery = true, value = "select rd.id as id , rd.name roleName from role_details rd "
			+ "left join group_role gr on rd.id = gr.role_id\r\n"
			+ "left join group_details gd on gd.id = gr.group_id\r\n"
			+ "left join user_groups ug on ug.group_id = gd.id\r\n"
			+ "left join user_details ud on ud.id = ug.user_id\r\n" + "where ud.email= :username")
	Set<RoleSummary> findRolesNameByUserName(@Param("username") String username);
	
	@Query(nativeQuery = true, value = "DELETE FROM role_page_access rpa WHERE rpa.role_id = :roleId")
	@Modifying
	void deleteByRole_Page_Access(@Param("roleId") String roleId);
	
	@Query(nativeQuery = true, value = "DELETE FROM group_role gr WHERE gr.role_id = :roleId")
	@Modifying
	void deleteByGroup_Role(@Param("roleId") String roleId);

}
