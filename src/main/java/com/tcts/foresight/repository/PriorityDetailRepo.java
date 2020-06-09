package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.PriorityDetailsEntity;

public interface PriorityDetailRepo extends  JpaRepository<PriorityDetailsEntity, Long>,CrudRepository<PriorityDetailsEntity, Long>  {

	@Query(value = "select * from priority_details pty where (pty.impact_id =:impactId) and (pty.urgency_id =:urgencyId) and (pty.name=:name ) and (pty.module is not null or pty.module =:module)", nativeQuery = true)
    PriorityDetailsEntity findImpactIdUrgencyIdByModule(@Param("impactId") Long impactId,@Param("urgencyId") Long urgencyId,@Param("name") String name,@Param("module") String module);

	PriorityDetailsEntity findNameByImpactId_IdAndUrgencyId_IdAndModule(Long impactId, Long urgencyId, String module);
	
	PriorityDetailsEntity findNameByImpactId_nameAndUrgencyId_name(String impact, String  urgency);
	
	
	  @Query(value ="select distinct(name) from priority_details (pty where pty.name =:name) and (pty.module is not null or pty.module =:module)", nativeQuery = true)    
	  List<PriorityDetailsEntity> findNameByModule(@Param("name") String name,@Param("module") String module);
	  
	  @Query( value="SELECT * FROM priority_details",nativeQuery = true)
	  List<PriorityDetailsEntity> getAllDistinctName();
	  
	  @Query( value="SELECT name FROM priority_details where id=:id",nativeQuery = true)
	  String findNameById(@Param("id") Long id);

	  @Query( value="SELECT * FROM priority_details where impact_id=:impactId And urgency_id=:urgencyId",nativeQuery = true)
	  PriorityDetailsEntity findNameByImpactId_IdAndUrgencyId(@Param("impactId")Long impactId, @Param("urgencyId")Long urgencyId);
	
}
