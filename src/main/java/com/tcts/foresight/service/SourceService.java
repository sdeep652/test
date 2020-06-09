package com.tcts.foresight.service;

import java.util.List;

import com.tcts.foresight.entity.SourceDetailsEntity;

public interface SourceService {
	
	public List<SourceDetailsEntity> getSource(String module);
	
	public List<SourceDetailsEntity> getAllSource();

	
}
