package com.tcts.foresight.service;


import java.util.List;

import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;

public interface PriorityService {

		public PriorityDetailsEntity addPriority(PriorityDetailsEntity priority);
		public PriorityDetailsEntity updatePriority(Long priorityId,PriorityDetailsEntity priority) throws ResourceNotFoundException;
		public PriorityDetailsEntity checkDuplicatePriority(Long impactId,Long urgencyId,String name, String module);
		public List<PriorityDetailsEntity> getAllPriority();
		public List<PriorityDetailsEntity> getPriorityList(String module);
		public void deletePriority(Long id);
		public PriorityDetailsEntity getPriorityName(Long impactId, Long urgencyId, String module);
		 public List<PriorityDetailsEntity> getAllPriorityName(String module, String Name);
		public PriorityDetailsEntity getPriorityNameForPM(Long impactId, Long urgencyId);
		
}
