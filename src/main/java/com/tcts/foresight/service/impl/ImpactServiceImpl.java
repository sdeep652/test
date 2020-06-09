package com.tcts.foresight.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.ImpactDetailsEntity;
import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.entity.UrgencyDetailsEntity;
import com.tcts.foresight.repository.ImpactDetailsRepo;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.repository.UrgencyDetailsRepo;
import com.tcts.foresight.service.ImpactService;

@Service
@Transactional
public class ImpactServiceImpl implements ImpactService {
	Logger logger = LoggerFactory.getLogger(ImpactServiceImpl.class); 

	@Autowired
	private ImpactDetailsRepo impactDetailsRepo;

	@Autowired
	private UrgencyDetailsRepo urgencyDetailsRepo;

	@Autowired
	private PriorityDetailRepo priorityDetailRepo;

	@Override
	public List<ImpactDetailsEntity> getAllImpactList(String module) {
		return impactDetailsRepo.findAll().stream().filter(name -> name.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}

	@Override
	public List<UrgencyDetailsEntity> getAllUrgencyList(String module) {
		return urgencyDetailsRepo.findAll().stream().filter(name -> name.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}

	@Override
	public List<PriorityDetailsEntity> getAllPriorityName(String module) {
		return priorityDetailRepo.findAll().stream().filter(name -> name.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}

	@Override
	public List<PriorityDetailsEntity> getAllDistinctName() {
		List<PriorityDetailsEntity> priorityDetailsEntityList = priorityDetailRepo.getAllDistinctName();
		List<PriorityDetailsEntity> priorityFinalList = new ArrayList<PriorityDetailsEntity>();
		HashMap<String, PriorityDetailsEntity> uniquePriorityNameMap = new HashMap<String, PriorityDetailsEntity>();

		for (PriorityDetailsEntity priorityDetailsEntity : priorityDetailsEntityList) {

			if (uniquePriorityNameMap.containsKey(priorityDetailsEntity.getName())) {

			} else {
				uniquePriorityNameMap.put(priorityDetailsEntity.getName(), priorityDetailsEntity);
			}
		}

		for (String key : uniquePriorityNameMap.keySet()) {
			priorityFinalList.add((PriorityDetailsEntity) uniquePriorityNameMap.get(key));
		}
		//logger.info("uniquePriorityName : " + priorityFinalList);
		return priorityFinalList;
	}

	@Override
	public List<ImpactDetailsEntity> getAllImpactListForPM() {
		return impactDetailsRepo.findAll();
		
	}

	@Override
	public List<UrgencyDetailsEntity> getAllUrgencyListForPM() {
		return urgencyDetailsRepo.findAll();
	}

	
}
