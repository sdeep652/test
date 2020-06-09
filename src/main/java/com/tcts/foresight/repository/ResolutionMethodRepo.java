package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.ResolutionMethodEntity;

public interface ResolutionMethodRepo extends JpaRepository<ResolutionMethodEntity, Long> {

	@Query(value = "select * from resolution_method_details rmd where (UPPER(rmd.name) = UPPER(:name) ) and (rmd.module is not null or rmd.module =:module)", nativeQuery = true)
	ResolutionMethodEntity findNameByModule(@Param("name") String name, @Param("module") String module);
	
	@Query(value = "select name from resolution_method_details  where id=:id", nativeQuery = true)
	String findNameById(@Param("id") Long id); 


}