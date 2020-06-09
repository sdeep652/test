package com.tcts.foresight.service;

import java.util.List;

import javax.validation.Valid;

import com.tcts.foresight.entity.ResolutionTypeEntity;
import com.tcts.foresight.entity.StatusDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsRemarkEntity;

public interface StatusService {

	public List<StatusDetailsEntity> getStatusforRemark();
	public List<StatusDetailsRemarkEntity> getAllStatusRemarkList(String module);
	public List<StatusDetailsEntity> getAllStatus();

	public StatusDetailsRemarkEntity addStatusRemark(StatusDetailsRemarkEntity status);
	public @Valid StatusDetailsRemarkEntity updateStatusRemark(Long id, StatusDetailsRemarkEntity status);
	public void deleteStatusRemark(Long id);
	public StatusDetailsRemarkEntity checkDuplicateStatus(String status, String remark, String module);
	public List<StatusDetailsEntity> getAllStatusIncident();
	public List<ResolutionTypeEntity> getAllStatusResolutiontype(String module);
	public List<StatusDetailsRemarkEntity> fetchStatusDetails(String status,String module);
	public List<StatusDetailsEntity> getAllUniqueStatus();
	public List<StatusDetailsEntity> getStatusCloseCancelled();
	
}
