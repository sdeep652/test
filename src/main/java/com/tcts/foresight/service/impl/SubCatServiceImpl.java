package com.tcts.foresight.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcts.foresight.entity.SubCatDetailsEntity;
import com.tcts.foresight.repository.SubCatDetailsRepo;
import com.tcts.foresight.service.SubCatService;

public class SubCatServiceImpl implements SubCatService{
	Logger logger = LoggerFactory.getLogger(SubCatServiceImpl.class);

	@Autowired
	SubCatDetailsRepo subCatDetailsRepo;

	@Override
	public List<SubCatDetailsEntity> getAllsubCatList() {
		return subCatDetailsRepo.findAll();
	}

	@Override
	public List<SubCatDetailsEntity> getcategoryList() {
		// TODO Auto-generated method stub
		return null;
	}

}
