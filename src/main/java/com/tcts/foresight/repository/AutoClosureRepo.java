package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.AutoClosureDetailsEntity;

public interface AutoClosureRepo extends JpaRepository<AutoClosureDetailsEntity, Long> {

	List<AutoClosureDetailsEntity> findByModule(String module);

	AutoClosureDetailsEntity findByCatDetails_IdAndPriorityDetails_NameAndModule(Long catId, String priorityName,
			String module);

	@Query(nativeQuery = true, value = "SELECT autoclose.time_in_mins autoCloseTime FROM public.autoclosure_details autoclose "
			+ "left join category_details cat on autoclose.cat_id = cat.id left join priority_details priority on autoclose.priority_id = priority.id "
			+ "WHERE cat.id is null and priority.name is null and autoclose.module =:module")
	String findAutoClosureTimeByModule(@Param("module") String module);

	@Modifying
	@Query(nativeQuery = true, value = "delete from autoclosure_details where cat_id= :catId")
	void deletebyCatId(@Param("catId") Long catId);

	@Query(nativeQuery = true , value= "select * from autoclosure_details where cat_id= :catId")
	List<AutoClosureDetailsEntity> findbyCatId(@Param("catId") Long catId);
	
}
