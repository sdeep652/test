package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.IncidentNotificationDetailsEntity;
import com.tcts.foresight.entity.ProgressBasedNotificationEntity;

@Repository
public interface IncidentProgressNotificationRepo extends JpaRepository<ProgressBasedNotificationEntity,Long> {

	List<ProgressBasedNotificationEntity> findByModule(String module);

	ProgressBasedNotificationEntity findFirstByOrderByIdAsc();
	
	

}
