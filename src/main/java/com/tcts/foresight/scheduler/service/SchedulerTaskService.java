package com.tcts.foresight.scheduler.service;

import java.util.List;

import javax.validation.Valid;

import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;

public interface SchedulerTaskService {
	IncidentSLAHistoryEntity createIncidentSLA(IncidentSLAHistoryEntity incSLAHEntity);
	public void completeSLAStatus(String incidentID, String responseSLA);
	List<IncidentSLAHistoryEntity> fetchIncidentSLADetails(String incidentID);

	IncidentSLAHistoryEntity createIncidentSLADhiraj(@Valid IncidentSLAHistoryEntity incSlaHisEntity);

}
