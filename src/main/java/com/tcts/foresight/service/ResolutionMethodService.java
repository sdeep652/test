package com.tcts.foresight.service;

import java.util.List;

import com.tcts.foresight.entity.ResolutionMethodEntity;

public interface ResolutionMethodService {

	public List<ResolutionMethodEntity> getAllResolutionMethod(String module);
	public ResolutionMethodEntity addResolutionMethod(ResolutionMethodEntity resolution);
	public ResolutionMethodEntity updateResolutionMethod(Long id, ResolutionMethodEntity resolution);
	public void deleteResolutionMethod(Long id);
	public ResolutionMethodEntity checkDuplicateResolutionMethod(String name, String module);

}
