package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.ResolutionTypeEntity;

public interface ResolutionTypeRepo extends  JpaRepository<ResolutionTypeEntity, Long>,CrudRepository<ResolutionTypeEntity, Long>  {
	
	@Query(value = "SELECT resolutiontype FROM resolution_type_details  where id = :id", nativeQuery = true)
	String findResolutionTypeNameById(@Param("id") Long id); 

	
}
