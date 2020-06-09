package com.tcts.foresight.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.service.JbpmProcessService;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/process")
public class JbpmProcessController {
	Logger logger = LoggerFactory.getLogger(JbpmProcessController.class);
	@Autowired
	private JbpmProcessService jbpmProcessService;
	
	@PostMapping("createinstance/{containerId}/{processId}")
	public String createProcessInstance(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @PathVariable("processId") String processId,
			@RequestParam("client") String clientCode, String requestPayload) {
		//logger.info("inside create instance");
		return jbpmProcessService.createProcessInstance(authToken,containerId,processId,clientCode,requestPayload);
	}
	
	@GetMapping("gettaskId/{processInstanceId}")
	public String getTaskId(@RequestHeader("Authorization") String authToken,
			@PathVariable("processInstanceId") String processInstanceId) {
		    //logger.info("inside gettaskId");
		return jbpmProcessService.getTaskId(authToken,processInstanceId);		
	}
}
