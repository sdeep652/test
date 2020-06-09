package com.tcts.foresight.scheduler.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.SLAConditionValuesEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.repository.SLAConditionValuesRepo;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.repository.SLAConfigurationRepo;
import com.tcts.foresight.scheduler.service.SLAConfigurationService;
import com.tcts.foresight.util.StringUtil;

@Service
@Transactional
public class SLAConfigurationServiceImpl implements SLAConfigurationService {

	Logger logger = LoggerFactory.getLogger(SLAConfigurationServiceImpl.class);

	@Autowired
	SLAConfigurationRepo slaConfigRepo;

	@Autowired
	SLAConditionValuesRepo slaCondtnValRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SLAConfigurationEntity createSLAConfig(SLAConfigurationEntity slaConfigReq) {
		if (StringUtil.isNullOrEmpty(slaConfigReq.getIsActive())) {
			slaConfigReq.setIsActive("false");
		}
		return slaConfigRepo.save(slaConfigReq);
	}

	@Override
	public SLAConfigurationEntity updateSLAConfig(Long slaConfigId, SLAConfigurationEntity slaConfigReq) {
		SLAConfigurationEntity returnSlaConfig = null;
		try {
			returnSlaConfig = slaConfigRepo.findById(slaConfigId).map(slaConfig -> {
				return slaConfigRepo.save(slaConfigReq);
			}).orElseThrow(() -> new ResourceNotFoundException("SLA Config Id:-  " + slaConfigId + " not found"));
		} catch (ResourceNotFoundException e) {
			logger.error("Exception occured in updateSLAConfig: " + e.getMessage(), e);

		}
		return returnSlaConfig;
	}

	@Override
	public List<SLAConfigurationEntity> fetchSLAConfingDetails(String module) {
		return slaConfigRepo.findSLAConfingByModule(module);
	}

	@Override
	public String fetchSlaName(String slaName) {
		String name = slaConfigRepo.findBySlaName(slaName);
		String stringToReturn = null;
		if (StringUtil.isNotNullNotEmpty(name) && name.equalsIgnoreCase(slaName)) {
			stringToReturn = name;
		}
		return stringToReturn;

	}

	@Override
	public void deleteSLAConfigById(Long slaConfigId) {
		slaConfigRepo.deleteById(slaConfigId);
	}

	@Override
	public List<SLAConditionValuesEntity> fetchSlaConditions() {
		return slaCondtnValRepo.findAll();
	}

	@Override
	public List<SLAConfigurationEntity> fetchSlaTargetDetails(String slaTarget, String module) {
		return slaConfigRepo.fetchSlaByTarget(slaTarget, module);
	}

	@Override
	public List<SLAConfigurationEntity> fetchActiveSLA() {
		// TODO Auto-generated method stub
		return slaConfigRepo.findByIsActive("true");
	}

	@Override
	public List<SLAConfigurationEntity> fetchAttachedSla(List<String> slaId) {
		// TODO Auto-generated method stub
		return null;
	}

}
