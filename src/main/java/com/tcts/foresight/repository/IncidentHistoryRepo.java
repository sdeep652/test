package com.tcts.foresight.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.IncidentHistoryEntity;

@Repository
public interface IncidentHistoryRepo extends JpaRepository<IncidentHistoryEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM incident_history where ticket_id =:ticket_id order by updated_date DESC")
	List<IncidentHistoryEntity> findByticketID(@Param("ticket_id") String incidentID);
	
	@Query(value="select * from incident_history where ticket_id=:incidentID and field_name='status'",nativeQuery = true )
	List<IncidentHistoryEntity> findAllStatusById(@Param("incidentID")String incidentID);

	@Query(value="select ticket_id from incident_history where old_value in(:groupNames) or new_value in(:groupNames)",nativeQuery = true )
	List<String> fetchTicketIdByGroupVisited(@Param("groupNames") String groupNames);	
	
	List<IncidentHistoryEntity> findByIncidentID(String incidentID);
	
	
	@Query(nativeQuery = true, value = "Select * from incident_history Where history_id IN( SELECT history_id FROM incident_history where ticket_id =:ticket_id And field_name='status' order by updated_date DESC LIMIT 1)")
	IncidentHistoryEntity findByticketID1(@Param("ticket_id") String incidentID);

	
	
	@Query(nativeQuery = true, value = "Select * from incident_history Where history_id IN( SELECT history_id FROM incident_history where ticket_id =:ticket_id  order by updated_date DESC LIMIT 1)")
	IncidentHistoryEntity checkForOtherFieldUpdation(@Param("ticket_id") String incidentID);
	
	Optional<IncidentHistoryEntity> findFirstByIncidentIDAndOldValue(String incidentID , String oldValue);
 
		
}
