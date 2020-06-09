package com.tcts.foresight.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.UserDetailsEntity;

public interface UserRepo extends CrudRepository<UserDetailsEntity, Long>, JpaRepository<UserDetailsEntity, Long> {

	@Query("SELECT user.email FROM UserDetailsEntity user where user.email = :userName")
	String findByEmail(@Param("userName") String userName);

	@Query("SELECT user.userTheme FROM UserDetailsEntity user where user.email = :userName")
	String findUserThemeByEmail(@Param("userName") String userName);

	@Query("SELECT user.fullName FROM UserDetailsEntity user where user.email = :userName")
	String findFullNameByEmail(@Param("userName") String userName);
	
	@Query("SELECT user.userId FROM UserDetailsEntity user where user.email = :userName")
	String userIdByEmail(@Param("userName") String userName);

	Optional<UserDetailsEntity> findUserByEmail(String userName);
	
	  @Query(nativeQuery = true, value="SELECT * FROM user_details  where email = :userName")
	  UserDetailsEntity findByEmail1(@Param("userName") String userName);
	
	List<UserDetailsSummary> findUserByGroupList_IdAndGroupList_GroupName(String groupId, String groupName);

	@Query(nativeQuery = true, value = "select ud.email as email, ud.id , ud.user_id as userId , ud.firstName as firstName, ud.lastName as lastName, ud.fullName as fullName from group_details gd \r\n"
			+ "join user_groups ug on gd.id = ug.group_id " + "join user_details ud on ud.id = ug.user_id\r\n"
			+ "where gd.id = :groupId")
	Set<UserDetailsSummary> findByMultipleGroupId(@Param("groupId") String groupId);
	
	@Query(value = "SELECT fullname FROM user_details where id = :id", nativeQuery = true)
	String findUserNameByID(@Param("id") Long id);
	
	@Query(value = "SELECT email FROM user_details where id = :id", nativeQuery = true)
	String findUserEmailByID(@Param("id") Long id); 
	
	@Query(value = "select  Distinct pd.module from user_details ud \r\n" + 
			"join user_groups ug on ud.id = ug.user_id\r\n" + 
			"join group_details gd on gd.id = ug.group_id\r\n" + 
			"join group_role gr on gd.id = gr.group_id\r\n" + 
			"join role_details rd on rd.id = gr.role_id\r\n" + 
			"join role_page_access rpa on rpa.role_id = rd.id\r\n" + 
			"join pagenames_details pd on pd.id = rpa.page_id\r\n" + 
			"where ud.email = :userName", nativeQuery = true)
	List<String> findModulebyUsername(@Param("userName") String userName);
	
	UserDetailsSummary findByAuthToken(String authToken);
	

}
