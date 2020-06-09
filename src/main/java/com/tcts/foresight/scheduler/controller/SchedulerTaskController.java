package com.tcts.foresight.scheduler.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;
import com.tcts.foresight.scheduler.service.SLAHistoryService;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/incident/sla")
public class SchedulerTaskController {

	// this controller is only for testing, UI is not using this API.

	@Autowired
	SLAHistoryService schedulerTaskSer;

	@PostMapping("/create")
	public IncidentSLAHistoryEntity createIncidentSLA(@Valid @RequestBody IncidentSLAHistoryEntity incSlaHisEntity) {
		return schedulerTaskSer.createIncidentSLA(incSlaHisEntity);
	}

	@GetMapping("/fetchIncidentSLADetails/{incidentID}")
	public List<IncidentSLAHistoryEntity> fetchIncidentSLADetails(@PathVariable String incidentID) {
		return schedulerTaskSer.fetchIncidentSLADetails(incidentID);
	}

	@GetMapping("/completeSLA/{incidentID}")
	public void completeSLA(@PathVariable String incidentID) {
		schedulerTaskSer.completeSLAStatus(incidentID, Constant.SLA_TARGET_RESPONSESLA);
	}
	
	

}
