package com.tcts.foresight.scheduler.service;

import java.util.List;

import javax.validation.Valid;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;

public interface SLAHistoryService {

	
//dummy methods for testing
	IncidentSLAHistoryEntity createIncidentSLA(@Valid IncidentSLAHistoryEntity incSlaHisEntity);
	void completeSLAStatus(String incidentID, String slaTypeResponsesla);
	List<IncidentSLAHistoryEntity> fetchIncidentSLADetails(String incidentID);
	//dummy methods for testing ends
	
	

	IncidentSLAHistoryEntity saveIncidentSLA(@Valid IncidentSLAHistoryEntity incSlaHisEntity);

	void createResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	void completeResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	void deleteSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	void cancelResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	void checkSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);
	
	void checkStartConditionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	
	void createResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);

	void completeResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);
	
	void cancelResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);
	
	void pauseResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW);


	


}
