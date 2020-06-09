package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tcts.foresight.entity.StatusDetailsEntity;

public interface StatusDetailsRepo extends  JpaRepository<StatusDetailsEntity, Long>,CrudRepository<StatusDetailsEntity, Long>  {

	
}
