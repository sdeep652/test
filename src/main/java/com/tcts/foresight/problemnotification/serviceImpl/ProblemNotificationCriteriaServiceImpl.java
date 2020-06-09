package com.tcts.foresight.problemnotification.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.problemnotification.entity.ProblemNotificationCriteriaEntity;
import com.tcts.foresight.problemnotification.repository.ProblemNotificationCriteriaRepo;

@Service
public class ProblemNotificationCriteriaServiceImpl {

	
	@Autowired
	private ProblemNotificationCriteriaRepo lProblemNotificationCriteriaRepo;
	
	public ProblemNotificationCriteriaEntity addnotificationCriteria(ProblemNotificationCriteriaEntity jsonPayload) {
		return lProblemNotificationCriteriaRepo.save(jsonPayload);
	}

	public ProblemNotificationCriteriaEntity updateNotificationCriteria(ProblemNotificationCriteriaEntity jsonPayload) {
		
		lProblemNotificationCriteriaRepo.deleteById(jsonPayload.getId());
		return lProblemNotificationCriteriaRepo.save(jsonPayload);
	}

	public List<ProblemNotificationCriteriaEntity> fetchAllNotificationCriteria() {
		
		return lProblemNotificationCriteriaRepo.findAll();
	}

	public Optional<ProblemNotificationCriteriaEntity> fetchAllNotificationCriteriaById(Long id) {
		
		return lProblemNotificationCriteriaRepo.findById(id);
	}

	public void deletNotificationCriteria(Long id) {
		
		lProblemNotificationCriteriaRepo.deleteById(id);
	}
	
	

}
