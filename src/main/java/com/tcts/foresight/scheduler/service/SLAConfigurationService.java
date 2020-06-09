package com.tcts.foresight.scheduler.service;

import java.util.List;

import com.tcts.foresight.entity.SLAConditionValuesEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;

public interface SLAConfigurationService {

	SLAConfigurationEntity createSLAConfig(SLAConfigurationEntity slaConfiEReq);

	List<SLAConfigurationEntity> fetchSLAConfingDetails(String module);

	String fetchSlaName(String slaName);

	List<SLAConditionValuesEntity> fetchSlaConditions();

	void deleteSLAConfigById(Long slaConfigId);

	SLAConfigurationEntity updateSLAConfig(Long slaConfigId, SLAConfigurationEntity slaConfigReq);

	List<SLAConfigurationEntity> fetchSlaTargetDetails(String slaTarget, String module);

	List<SLAConfigurationEntity> fetchActiveSLA();

	List<SLAConfigurationEntity> fetchAttachedSla(List<String> slaId);

}
