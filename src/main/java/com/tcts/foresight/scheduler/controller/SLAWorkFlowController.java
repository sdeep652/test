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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.scheduler.entity.SLAWorkFlowEntity;
import com.tcts.foresight.scheduler.service.SLAWorkFlowService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/incident/sla/workflow")
public class SLAWorkFlowController {
	Logger logger = LoggerFactory.getLogger(SLAWorkFlowController.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private SLAWorkFlowService slaWrkFlwSer;

	@PostMapping("/create")
	public SLAWorkFlowEntity createSLAWorkFlow(@RequestBody SLAWorkFlowEntity slaWrkFlwEntityLst) {
		SLAWorkFlowEntity list = new SLAWorkFlowEntity();
		try {
			list = slaWrkFlwEntityLst;
			logger.info("Create SLA WorkFlow Json Body {} ", objMapper.writeValueAsString(list));
			list = slaWrkFlwSer.createSLAWorkFlow(list);
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in createSLAWorkFlow" + e.getMessage(),e);
		}
		return list;

	}

	@GetMapping("/ByModule/{module}")
	public List<SLAWorkFlowEntity> fetchSlaWorkFlowByModule(@PathVariable String module) {
		return slaWrkFlwSer.fetchSlaWorkFlowByModule(module);
	}
	
    //Test API for fetchSingleSlaWorkFlow
    @GetMapping("/fetchSingleSlaWorkFlow/{workflowid}")
    public SLAWorkFlowEntity fetchSingleSlaWorkFlow(@PathVariable Long workflowid) {
           return slaWrkFlwSer.fetchSingleSlaWorkFlow(workflowid);
    }


	@GetMapping("/byAll")
	public List<SLAWorkFlowEntity> fetchAllSlaWorkFlow() {
		return slaWrkFlwSer.fetchAllSlaWorkFlow();
	}

	@PutMapping("/update")
	public SLAWorkFlowEntity updateSLAWorkFlow(@RequestBody SLAWorkFlowEntity slaWrkFlwEntityLst)
			throws ResourceNotFoundException {
		SLAWorkFlowEntity list1 = null;
		try {

			logger.info("Create SLA WorkFlow Json Body {} ", objMapper.writeValueAsString(slaWrkFlwEntityLst));
			list1 = slaWrkFlwSer.updateworkFlow(slaWrkFlwEntityLst);
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in updateworkFlow" + e.getMessage(),e);
		}
		return list1;

	}

	@DeleteMapping("/delete/{workflowId}")
	public void deleteWorkflowById(@PathVariable Long workflowId) {
		slaWrkFlwSer.deleteWorkflowById(workflowId);
	}

	@GetMapping("/fetchSlaName/{slaName}")
	public String fetchSLAWorkfolwSlaName(@PathVariable String slaName) {
		return slaWrkFlwSer.fetchSlaName(slaName);
	}

}
