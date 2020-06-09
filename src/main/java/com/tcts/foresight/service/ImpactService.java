package com.tcts.foresight.service;

import java.util.List;

import com.tcts.foresight.entity.ImpactDetailsEntity;
import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.entity.UrgencyDetailsEntity;


public interface ImpactService {

		public List<ImpactDetailsEntity> getAllImpactList(String module);
		public List<UrgencyDetailsEntity> getAllUrgencyList(String module);
		List<PriorityDetailsEntity> getAllPriorityName(String module);
		List<PriorityDetailsEntity> getAllDistinctName();
		public List<ImpactDetailsEntity> getAllImpactListForPM();
		public List<UrgencyDetailsEntity> getAllUrgencyListForPM();



}
