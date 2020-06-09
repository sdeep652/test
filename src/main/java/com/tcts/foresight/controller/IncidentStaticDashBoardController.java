package com.tcts.foresight.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.service.impl.IncidentStaticDashboardServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/staticDashboard")
public class IncidentStaticDashBoardController {

	@Autowired
	IncidentStaticDashboardServiceImpl lSlaStaticDashboardService;

	@PostMapping("/slaBasedDashboards")
	public Map<String, HashMap<String, Object>> slaBasedDashboards(@RequestBody String jsonPayLoadMap) {

		return lSlaStaticDashboardService.getslaBasedDashboards(jsonPayLoadMap);
	}

	@PostMapping("/MTTRDashboards")
	public HashMap<String, Object> MTTRDashboards(@RequestBody String jsonPayLoadMap) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map = lSlaStaticDashboardService.getMTTRDashboards(jsonPayLoadMap);
		return map;
	}
	
	
	@PostMapping("/customerSatisfactionChart")
	public Map<String, Object> customerSatisfactionChart(@RequestBody String jsonPayLoadMap) {

		return lSlaStaticDashboardService.customerSatisfactionChart(jsonPayLoadMap);
	}

}
