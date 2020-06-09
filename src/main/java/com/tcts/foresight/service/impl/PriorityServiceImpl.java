package com.tcts.foresight.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.service.PriorityService;

@Service
@Transactional
public class PriorityServiceImpl implements PriorityService{
	Logger logger = LoggerFactory.getLogger(PriorityServiceImpl.class);

	@Autowired
	PriorityDetailRepo priorityDetailRepo;

	@Override
	public PriorityDetailsEntity addPriority(PriorityDetailsEntity priority) {
		return priorityDetailRepo.save(priority);
	}

	@Override
	public PriorityDetailsEntity updatePriority(Long priorityId, PriorityDetailsEntity priority) throws ResourceNotFoundException {
		return priorityDetailRepo.findById(priorityId)
				.map(priorityDtls -> {return priorityDetailRepo.save(priority);})
				.orElseThrow(() -> new ResourceNotFoundException("Priority Id " + priorityId + " not found"));
	}
	
	@Override
	public PriorityDetailsEntity checkDuplicatePriority(Long impactId,Long urgencyId,String name,String module) {
		PriorityDetailsEntity priorityDtls = null;
		try {
			//logger.info(" i m in checkDuplicatePriority impl");
			priorityDtls = priorityDetailRepo.findImpactIdUrgencyIdByModule(impactId,urgencyId,name,module);
		}catch (Exception e) {
			logger.error("Exception occur while in checkDuplicatePriority"+e.getMessage(),e);
		}
		return priorityDtls;
		
	}

	@Override
	public void deletePriority(Long id) {
		priorityDetailRepo.deleteById(id);

	}

	@Override
	public List<PriorityDetailsEntity> getAllPriority() {
		return priorityDetailRepo.findAll();
	}

	@Override
	public List<PriorityDetailsEntity> getAllPriorityName(String module,String name) {
		return priorityDetailRepo.findNameByModule(name,module);
	}
	
	@Override
	public List<PriorityDetailsEntity> getPriorityList(String module) {
		return priorityDetailRepo.findAll().stream()
				.filter(priorityDtls -> priorityDtls.getModule() != null && priorityDtls.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}

	@Override
	public PriorityDetailsEntity getPriorityName(Long impactId, Long urgencyId, String module) {
		return priorityDetailRepo.findNameByImpactId_IdAndUrgencyId_IdAndModule(impactId,urgencyId,module);
	}

	@Override
	public PriorityDetailsEntity getPriorityNameForPM(Long impactId, Long urgencyId) {
		System.out.println("here");
		return priorityDetailRepo.findNameByImpactId_IdAndUrgencyId(impactId,urgencyId);
	}
}
