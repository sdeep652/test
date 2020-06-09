package com.tcts.foresight.scheduler.service;

import java.util.List;

import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.scheduler.entity.SLAWorkFlowEntity;

public interface SLAWorkFlowService {

	SLAWorkFlowEntity createSLAWorkFlow(SLAWorkFlowEntity slaWorkFlowEntity);

	SLAWorkFlowEntity updateworkFlow(SLAWorkFlowEntity slaWrkFlwEntityLst) throws ResourceNotFoundException;

	void deleteWorkflowById(Long workflowId);

	List<SLAWorkFlowEntity> fetchAllSlaWorkFlow();

	List<SLAWorkFlowEntity> fetchSlaWorkFlowByModule(String module);

	String fetchSlaName(String slaName);

	SLAWorkFlowEntity fetchSingleSlaWorkFlow(Long workflowid);

}
