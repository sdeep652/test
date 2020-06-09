package com.tcts.foresight.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.SourceDetailsEntity;
import com.tcts.foresight.repository.SourceDetailsRepo;
import com.tcts.foresight.service.SourceService;

@Service
@Transactional
public class SourceServiceImpl implements SourceService{
	Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);

	@Autowired
	private SourceDetailsRepo sourceDtlsRepo;

	@Override
	public List<SourceDetailsEntity> getSource(String module) {
				List<SourceDetailsEntity> list=sourceDtlsRepo.findByModule(module);
		return list;
	}

	public List<SourceDetailsEntity> getAllSource(){
		List<SourceDetailsEntity> list=sourceDtlsRepo.findAll();
		return list;
	}
} 
