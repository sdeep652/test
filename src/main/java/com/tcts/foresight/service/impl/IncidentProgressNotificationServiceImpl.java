package com.tcts.foresight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.ProgressBasedNotificationEntity;
import com.tcts.foresight.repository.IncidentProgressNotificationRepo;

@Service
public class IncidentProgressNotificationServiceImpl {
	

	@Autowired
	private IncidentProgressNotificationRepo incidentProgressNotificationRepo;

	public ProgressBasedNotificationEntity insertNotificationDetails(
			ProgressBasedNotificationEntity progressBasedNotificationEntity) {
		return incidentProgressNotificationRepo.save(progressBasedNotificationEntity);	}

	public List<ProgressBasedNotificationEntity> getAllNotificationDetails(String module) {
		
		return incidentProgressNotificationRepo.findByModule(module);
	}

	public ProgressBasedNotificationEntity editNotificationDetails(Long id, ProgressBasedNotificationEntity progressBasedNotificationEntity) {
		ProgressBasedNotificationEntity lprogressBasedNotificationEntity=incidentProgressNotificationRepo.findById(id).get();
		lprogressBasedNotificationEntity.setEmailList(progressBasedNotificationEntity.getEmailList());
		lprogressBasedNotificationEntity.setPhoneNumberList(progressBasedNotificationEntity.getPhoneNumberList());
		incidentProgressNotificationRepo.save(lprogressBasedNotificationEntity);
		
		return lprogressBasedNotificationEntity;
	    
	}

	public void deleteNotificationDetails(Long id) {
		
		incidentProgressNotificationRepo.deleteById(id);
		
	}

}
