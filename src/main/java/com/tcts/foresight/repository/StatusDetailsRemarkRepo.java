package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.StatusDetailsRemarkEntity;

public interface StatusDetailsRemarkRepo extends  JpaRepository<StatusDetailsRemarkEntity, Long>,CrudRepository<StatusDetailsRemarkEntity, Long>  {

	@Query(value = "select * from status_remark sts where (sts.status =:status) and (UPPER(sts.remark) = UPPER(:remark) ) and (sts.module is not null or sts.module =:module)", nativeQuery = true)
	StatusDetailsRemarkEntity findStatusRemarkByModule(@Param("status") String status,@Param("remark")  String remark,@Param("module") String module);
	
	@Query(value = "select * from status_remark sts where (sts.status =:status) and (sts.module is not null and sts.module =:module)", nativeQuery = true)
	List<StatusDetailsRemarkEntity> findStatusByModule(@Param("status") String status,@Param("module") String module);
}

