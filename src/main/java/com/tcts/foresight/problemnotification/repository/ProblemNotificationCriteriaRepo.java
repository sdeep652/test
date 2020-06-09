package com.tcts.foresight.problemnotification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.problemnotification.entity.ProblemNotificationCriteriaEntity;

@Repository
public interface ProblemNotificationCriteriaRepo extends JpaRepository<ProblemNotificationCriteriaEntity, Long>{

	
	List<ProblemNotificationCriteriaEntity> findByIsActive(String value);
	
	
	

}
