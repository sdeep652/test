package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.SourceDetailsEntity;

public interface SourceDetailsRepo extends JpaRepository<SourceDetailsEntity, Long> {

	List<SourceDetailsEntity> findByModule(String module);

	@Query(value = "select name from source_details where id =:id",nativeQuery = true)
	String findSourceNameById(@Param("id") Long id);
}
