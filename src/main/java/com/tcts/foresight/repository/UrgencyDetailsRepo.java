package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tcts.foresight.entity.UrgencyDetailsEntity;
public interface UrgencyDetailsRepo extends  JpaRepository<UrgencyDetailsEntity, Long>,CrudRepository<UrgencyDetailsEntity, Long>{  


}