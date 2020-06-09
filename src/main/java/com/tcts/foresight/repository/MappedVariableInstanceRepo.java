package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;

@Repository
public interface MappedVariableInstanceRepo extends JpaRepository<MappedVariableInstanceLogVO, String>,
		PagingAndSortingRepository<MappedVariableInstanceLogVO, String> {

	@Query(nativeQuery = true, value = "SELECT * FROM mapped_variable_instance_log  where ticket_id = :ticket_id")
	MappedVariableInstanceLogVO findByticketID(@Param("ticket_id") String ticket_id);

	MappedVariableInstanceLogVO findByIncidentID(String incidentID);

	@Query(nativeQuery = true, value = "SELECT processinstance_id,ticket_id FROM mapped_variable_instance_log  where module = ?1 AND status NOT IN (?2) AND status NOT IN(?3)")
	List<String> findBymoduleandStatus(String module, String statusClosed, String statusCancelled);

	@Query(value = "SELECT ticket_id FROM mapped_variable_instance_log where status NOT IN(:status)", nativeQuery = true)
	List<String> findByStatus(@Param("status") List<String> status);

	@Query(value = "SELECT * FROM mapped_variable_instance_log where parent_ticketid  IN(:child)", nativeQuery = true)
	List<MappedVariableInstanceLogVO> fetchChilds(@Param("child") String parentTicketId);

	@Query(value = "SELECT * FROM mapped_variable_instance_log where ticket_id  IN(:parent) and parent_ticketid is not null", nativeQuery = true)
	MappedVariableInstanceLogVO fetchParents(@Param("parent") String childTicketId);

	@Query(value = "SELECT parent_ticketid FROM mapped_variable_instance_log where ticket_id  IN(:tId)", nativeQuery = true)
	String fetchParentsParent(@Param("tId") String incidentID);

	@Query(value = "SELECT ticket_id FROM mapped_variable_instance_log where parent_ticketid  IN(:pId)", nativeQuery = true)
	List<String> fetchParentschild(@Param("pId") String incidentID);

	// Fetch ticket with status not cancelled and closed and assignment Group

	@Query(value = "SELECT ticket_id FROM mapped_variable_instance_log where status NOT IN(:Status1,:Status2) AND group_name IN(:group) order by ticket_id asc", nativeQuery = true)
	List<String> getIncidentListForParentChild(@Param("Status1") String status1, @Param("Status2") String status2,
			@Param("group") String group);

	@Query(value = "select ticket_id from mapped_variable_instance_log order by mapped_variable_instance_log.created_date desc limit 1", nativeQuery = true)
	String findNewTicketId();

	// ageing
	@Query(value = "select created_date from mapped_variable_instance_log  where ticket_id IN(:incidentID)", nativeQuery = true)
	String getIncidentAgeing(@Param("incidentID") String incidentID);

	@Query(value = "select resolved_date from mapped_variable_instance_log  where ticket_id IN(:incidentID)", nativeQuery = true)
	String getIncidentAgeings(@Param("incidentID") String incidentID);

	@Query(value = "SELECT distinct createdby_full_name FROM public.mapped_variable_instance_log where createdby_full_name is NOT NULL", nativeQuery = true)
	List<String> getCreatedBy();

	@Query(value = "SELECT  distinct resolvedby from public.mapped_variable_instance_log where resolvedby is NOT NULL AND resolvedby != ''", nativeQuery = true)
	List<String> getResolvedBy();

	
	@Query(value = "SELECT * FROM mapped_variable_instance_log where group_name IN (:groupName) AND module=:module AND"
			+"( UPPER(ticket_id) LIKE UPPER(:searchParam) OR UPPER(assign_to) LIKE UPPER(:searchParam) OR UPPER(category) LIKE UPPER(:searchParam) "
			+" OR  UPPER(sub_category) LIKE UPPER(:searchParam) OR  UPPER(priority) LIKE UPPER(:searchParam) OR  UPPER(title) LIKE UPPER(:searchParam)"
			+" OR  UPPER(status) LIKE UPPER(:searchParam) OR UPPER(descriptions) LIKE UPPER(:searchParam) OR  UPPER(incident_type) LIKE UPPER(:searchParam)"
			+" OR  UPPER(configuration_item) LIKE UPPER(:searchParam) OR UPPER(resolution_method) LIKE UPPER(:searchParam) OR  UPPER(resolution_type) LIKE UPPER(:searchParam)"
			+" OR  UPPER(source) LIKE UPPER(:searchParam) OR UPPER(source_contact) LIKE UPPER(:searchParam) OR UPPER(group_name) LIKE UPPER(:searchParam)"
			+" OR  UPPER(createdby_full_name) LIKE UPPER(:searchParam))", nativeQuery = true)
	List<MappedVariableInstanceLogVO> fetchGlobalSearchData(@Param("groupName") List<String> groupName,@Param("searchParam") String searchParam,@Param("module") String module);

	
	
	@Query(value = "SELECT * FROM mapped_variable_instance_log  WHERE created_date BETWEEN :startDate AND :endDate AND category= :category AND sub_category= :subCategory", nativeQuery = true)
	List<MappedVariableInstanceLogVO> findIncidentBetweenDateRange(@Param("category") String category, @Param("subCategory") String subCategory,@Param("startDate") String startDate, @Param("endDate") String endDate);

//	@Query(value = "SELECT ticket_id FROM mapped_variable_instance_log where status NOT IN(:Status) AND group_name IN(:group) order by ticket_id asc",nativeQuery = true)
//	List<String> getIncidentListForParentChilds(@Param("Status")String mainStatus, @Param("group")String group);
//	

//	@Query(value = "SELECT ticket_id FROM mapped_variable_instance_log where status NOT IN(:Status) AND group_name IN(:group) order by ticket_id asc",nativeQuery = true)
//	List<String> getIncidentListForParentChild(@Param("Status")String status,@Param("group")String group);

//	@Query("update mapped_variable_instance_log u set u.parent_ticketid = :parentTicketId where u.ticket_id = :ticketId", nativeQuery = true)
//	void updateParentTicketId(@Param("parentTicketId")String parentTicketId, @Param("ticketId")String ticketId);

}
