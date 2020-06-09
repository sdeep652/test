package com.tcts.foresight.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.ResolutionMethodEntity;
import com.tcts.foresight.repository.ResolutionMethodRepo;
import com.tcts.foresight.service.ResolutionMethodService;
@Service
@Transactional
public class ResolutionMethodServiceImpl implements ResolutionMethodService{
	Logger logger = LoggerFactory.getLogger(ResolutionMethodServiceImpl.class);

	@Autowired
	ResolutionMethodRepo resolutionMetRepo;

	@Override
	public List<ResolutionMethodEntity> getAllResolutionMethod(String module) {
		return resolutionMetRepo.findAll().stream() 
				.filter(name ->name.getModule().equalsIgnoreCase(module)).collect(Collectors.toList());
	}
	
	@Override
	public ResolutionMethodEntity addResolutionMethod(ResolutionMethodEntity resolution) {	
		return resolutionMetRepo.save(resolution);
	}

	@Override
	public ResolutionMethodEntity updateResolutionMethod(Long id, ResolutionMethodEntity resolution) {
		ResolutionMethodEntity result= resolutionMetRepo.findById(id).get();
		//logger.info("my data in statusUP=" + result);
		if (result != null) {
			resolution.setId(id);
			resolutionMetRepo.save(resolution);

			return resolution;
		}
		return resolution;
	}

	@Override
	public void deleteResolutionMethod(Long id) {
		resolutionMetRepo.deleteById(id);
	}

	@Override
	public ResolutionMethodEntity checkDuplicateResolutionMethod(String name,String module) {
		ResolutionMethodEntity resolution = null;
		try {
			resolution = resolutionMetRepo.findNameByModule(name,module);
		}catch (Exception e) {
			logger.error("Exception occur while in checkDuplicateResolutionMethod"+e.getMessage(),e);
		}
		return resolution;

	}


}
