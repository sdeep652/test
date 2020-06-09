package com.tcts.foresight.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.ProblemNotificationIncident;;

@Repository
public interface ProblemNotificationIncidentRepo extends JpaRepository<ProblemNotificationIncident, Long> {

	Optional<ProblemNotificationIncident> findByIncidentID(String incidentID);

	
	String findNameById(@Param("id") Long id); 

	
//	@Query(value = "select * from problem_notification_details_incidentwhere incident_id IN :query", nativeQuery = true)
//	List<ProblemNotificationIncident> gateAllIncidentsList(@Param("query") String query);

}
