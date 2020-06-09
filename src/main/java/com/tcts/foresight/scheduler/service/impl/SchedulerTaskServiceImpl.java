package com.tcts.foresight.scheduler.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;
import com.tcts.foresight.scheduler.repository.IncidentSLAHistoryRepo;
import com.tcts.foresight.scheduler.repository.SLAConfigurationRepo;
import com.tcts.foresight.scheduler.service.SchedulerTaskService;
import com.tcts.foresight.util.Constant;

@Service
public class SchedulerTaskServiceImpl implements SchedulerTaskService {
	Logger logger = LoggerFactory.getLogger(SchedulerTaskServiceImpl.class);

	@Autowired
	IncidentSLAHistoryRepo incidentSLAHRepo;

	@Autowired
	MappedVariableInstanceRepo mappedVarInsRepo;
	
	@Autowired
	SLAConfigurationRepo slaConfigRepo1;
	
	

	@Override
	public IncidentSLAHistoryEntity createIncidentSLA(IncidentSLAHistoryEntity incSLAHEntity) {
		IncidentSLAHistoryEntity returnIncSLAHisE = null;
		MappedVariableInstanceLogVO mappedVarInstLogVo = null;
		try {
			mappedVarInstLogVo = mappedVarInsRepo.findByIncidentID(incSLAHEntity.getIncidentID());
			if (mappedVarInstLogVo.getStatus().equalsIgnoreCase(Constant.INCIDENT_STATUS_IN_PROGRESS)) {
				incSLAHEntity.setSlaStatus(Constant.SLA_STATUS_COMPLETED);
			}
			returnIncSLAHisE = incidentSLAHRepo.save(incSLAHEntity);
		} catch (Exception e) {
			logger.error("Exception occur while in createIncidentSLA" + e.getMessage(),e);
			
		}
		return returnIncSLAHisE;
	}

	@Override
	public List<IncidentSLAHistoryEntity> fetchIncidentSLADetails(String incidentID) {
		return incidentSLAHRepo.findByIncidentID(incidentID);
	}

	@Override
	public void completeSLAStatus(String incidentID, String responseSLA) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		// TODO Auto-generated method stub
		List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(incidentID,
				responseSLA);
		
		for(IncidentSLAHistoryEntity lIncidentSLAHistoryEntity:lIncidentSLAHistoryEntityList)
		{
			if (lIncidentSLAHistoryEntity != null) {
				
				lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_COMPLETED);
				
				String lastUpdatedDate = sdf.format(new Date());
				lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
				incidentSLAHRepo.save(lIncidentSLAHistoryEntity);
				//logger.info("Status is changed as complete");
			} else {
				//logger.info("incident not found with responseSLA type");
			}
		}
		

	}

	@Override
	public IncidentSLAHistoryEntity createIncidentSLADhiraj(@Valid IncidentSLAHistoryEntity incSlaHisEntity) {
		// TODO Auto-generated method stub
		return incidentSLAHRepo.save(incSlaHisEntity);
	}

}
