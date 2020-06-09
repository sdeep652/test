package com.tcts.foresight.scheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;

public interface SLAConfigurationRepo extends JpaRepository<SLAConfigurationEntity, Long> {

	List<SLAConfigurationEntity> findSLAConfingByModule(String module);

	@Query("SELECT slaConfig.slaName FROM SLAConfigurationEntity slaConfig where (LOWER(slaConfig.slaName) = LOWER(:slaName))")
	String findBySlaName(@Param("slaName") String slaName);
	
	
	// @Query("SELECT * FROM sla_configuration where id= :id")
	SLAConfigurationEntity findSlaById(Long originalSLAConfigId);

	@Query(value="select * from sla_configuration where sla_target =:slaTarget and module =:module", nativeQuery = true)
	List<SLAConfigurationEntity> fetchSlaByTarget(String slaTarget, String module);

	List<SLAConfigurationEntity> findByIsActive(String string);
	
	@Query(value = "SELECT * FROM sla_configuration where id IN(:id)", nativeQuery = true)
	List<SLAConfigurationEntity> fetchAttachedSla(@Param("id") List<Long> slaId);
	
	
	@Query(value = "select * from sla_configuration where id IN (select sla_config_id from sla_configuration_actions where"
			+ " condition_on = :condition and value LIKE %:value%)", nativeQuery = true)
	List<SLAConfigurationEntity> findBySLAContionOn(@Param("condition") String condition , @Param("value") String value);
	 

}
