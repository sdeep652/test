package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.PageNamesDetailsEntity;

public interface PageAccessRepo extends JpaRepository<PageNamesDetailsEntity, Long> {

	@Query(nativeQuery = true, value = "select pd.id , pd.pagename as pagename, pd.module  from pagenames_details pd \r\n"
			+ "left join role_page_access rpa on rpa.page_id = pd.id\r\n"
			+ "left join role_details rd on rd.id = rpa.role_id\r\n"
			+ "left join group_role gr on rd.id = gr.role_id\r\n"
			+ "left join group_details gd on gd.id = gr.group_id\r\n"
			+ "left join user_groups ug on ug.group_id = gd.id\r\n"
			+ "left join user_details ud on ud.id = ug.user_id\r\n" + "where ud.authtoken= :authToken \r\n"
			+ "group by pd.id, pagename,pd.module")
	List<PageSummary> findPageNameByAuthToken(@Param("authToken") String authToken);

	@Query(nativeQuery = true, value ="select pd.id as id ,pd.pagename as pagename, pd.module as module from pagenames_details pd \r\n" + 
			"join role_page_access rpa on rpa.page_id = pd.id\r\n" + 
			"join role_details rd on rd.id = rpa.role_id\r\n" + 
			"where pd.module = :module and rd.name IN (:roles)")
	List<PageNamesDetailsEntity> findPageAccessByRoleName(@Param("module") String module,@Param("roles") List<String> roles);
	
}
