package com.tcts.foresight.scheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;

@Repository
public interface IncidentSLAHistoryRepo extends JpaRepository<IncidentSLAHistoryEntity, Long>{
	
//	@Query(value= "SELECT DATE_PART('hour', cast(CURRENT_TIMESTAMP as time) - cast(CREATED_TIME as time)) * 60 + DATE_PART('minute', cast(CURRENT_TIMESTAMP as time) - cast(CREATED_TIME as time)) DIFFMIN FROM INCIDENT_SLA_HISTORY "
//			+ "WHERE sla_type =:slaType  and sla_status = :slaStatus", 
//			nativeQuery = true)
//	Long findDiff(@Param("slaType") String slaType,@Param("slaStatus") String slaStatus);	
	
	List<IncidentSLAHistoryEntity> findByIncidentID(String ticket_id);
	
	
	List<IncidentSLAHistoryEntity> findByIncidentIDAndSlaType(String incidentID, String slaType);
	
	@Query(value="select * from INCIDENT_SLA_HISTORY where ticket_id IN (:incidentIDList) and sla_type=:slaType",nativeQuery = true )
	List<IncidentSLAHistoryEntity> findByIncidentIDAndSlaTypeNative(@Param("incidentIDList") List<String> incidentIDList,@Param("slaType") String slaType);

	@Query(value="select * from INCIDENT_SLA_HISTORY where ticket_id=:incidentID and sla_type=:slaType order by id desc",nativeQuery = true )
	List<IncidentSLAHistoryEntity> findByIncidentIDAndSlaTypeDescOrder(@Param("incidentID")String incidentID, @Param("slaType")String slaType);
	
	@Query(value="select * from INCIDENT_SLA_HISTORY where ticket_id=:incidentID and sla_type=:slaType ",nativeQuery = true )
	IncidentSLAHistoryEntity findByIncidentIDAndSlaType1(@Param("incidentID")String incidentID, @Param("slaType")String slaType);
	
	
	
}
