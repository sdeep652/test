package com.tcts.foresight.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.service.IncidentReportService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/report")
public class IncidentReportController {
	
	@Autowired
	IncidentReportService incidentReportService;
	
	
	
	
	@PostMapping("/fetchIncidentAnalysisReport")
	public List<HashMap<String, Object>> getAllIncidentAnalysisReport(@RequestBody String payLoad){
		return incidentReportService.getAllIncidentAnalysisReport(payLoad);
	}	
	
	@PostMapping("/getAgeing/{incidentID}")
	public Long getIncidentAgeing(@PathVariable String incidentID) {
		return incidentReportService.getIncidentAgeing(incidentID);
		
	}
	
	
	@PostMapping("/fetchAgeingReport")
	public List<MappedVariableInstanceLogVO> fetchAgeingReport(@RequestBody String jsonPayLoadMap) {
		//logger.info("inside filtered job");
//		return incidentCreationService.filteredJobList(jsonPayLoadMap);
		
		return incidentReportService.fetchAgeingReport(jsonPayLoadMap);
	}
	
    @GetMapping("/fetchAllIncidentCreatedResolvedUser")
    public HashMap<String, List<String>> fetchAllIncidentCreatedResolvedUser(){
    	return incidentReportService.fetchAllIncidentCreatedResolvedUser();
    }
    
    @GetMapping("fetchAvailableColumnDisplay")
    public HashMap<String,String> fetchAvailableColumnDisplay(){
    	return incidentReportService.fetchAvailableColumnDisplay();
    }
	
}
