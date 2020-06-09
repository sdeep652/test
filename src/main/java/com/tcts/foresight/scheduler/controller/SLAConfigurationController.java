package com.tcts.foresight.scheduler.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tcts.foresight.entity.SLAConditionValuesEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.service.SLAConfigurationService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/incident/slaconfig")
public class SLAConfigurationController {

	Logger logger = LoggerFactory.getLogger(SLAConfigurationController.class);

	@Autowired
	private SLAConfigurationService slaConfigService;

	@PostMapping("/create")
	public SLAConfigurationEntity createSLAConfig(@RequestBody SLAConfigurationEntity slaConfigReq)
			throws JsonProcessingException {
		// logger.info("Create SLA Config JSON Req {}
		// ",objMapper.writeValueAsString(slaConfigReq));
		return slaConfigService.createSLAConfig(slaConfigReq);
	}

	@PutMapping("/update/{slaConfigId}")
	public SLAConfigurationEntity updateSLAConfig(@PathVariable Long slaConfigId,
			@RequestBody SLAConfigurationEntity slaConfigReq) throws JsonProcessingException {
		// logger.info("Update SLA Config JSON Req {}
		// ",objMapper.writeValueAsString(slaConfigReq));
		return slaConfigService.updateSLAConfig(slaConfigId, slaConfigReq);
	}

	@GetMapping("/fetchSLAConfingDetails/{module}")
	public List<SLAConfigurationEntity> fetchSLAConfingDetails(@PathVariable String module) {
		// logger.info("SLA Details of Module {} ",module);
		return slaConfigService.fetchSLAConfingDetails(module);
	}

	@GetMapping("/fetchSlaName/{slaName}")
	public String fetchSLAConfigSlaName(@PathVariable String slaName) {
		// logger.info("SLA Config of SLA Name JSON Req {} ",slaName);
		return slaConfigService.fetchSlaName(slaName);
	}

	@DeleteMapping("/deleteSLAConfigById/{slaConfigId}")
	public void deleteSLAConfigById(@PathVariable Long slaConfigId) {
		// logger.info("Delete SLA Config By Id JSON Req {} ",slaConfigId);
		slaConfigService.deleteSLAConfigById(slaConfigId);
	}

	@GetMapping("/fetchSlaConditions")
	public List<SLAConditionValuesEntity> fetchSlaConditions() {
		return slaConfigService.fetchSlaConditions();
	}

	@GetMapping("/fetchSlaByTarget/{slaTarget}/{module}")
	public List<SLAConfigurationEntity> fetchSlaTargetDetails(@PathVariable String slaTarget,
			@PathVariable String module) {
		// logger.info("SLA Details of Module {} ",module);
		return slaConfigService.fetchSlaTargetDetails(slaTarget, module);
	}

	@GetMapping("/fetchActiveSLA")
	public List<SLAConfigurationEntity> fetchActiveSLA() {
		// logger.info("SLA Details of Module {} ",module);
		return slaConfigService.fetchActiveSLA();
	}
}
