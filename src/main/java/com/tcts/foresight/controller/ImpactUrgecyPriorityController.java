package com.tcts.foresight.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.ImpactDetailsEntity;
import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.entity.UrgencyDetailsEntity;
import com.tcts.foresight.repository.ImpactDetailsRepo;
import com.tcts.foresight.service.ImpactService;
import com.tcts.foresight.service.PriorityService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/imapactUrgencyPriority")
public class ImpactUrgecyPriorityController {
	Logger logger = LoggerFactory.getLogger(ImpactUrgecyPriorityController.class);

	@Autowired
	ImpactService impactService;

	@Autowired
	ImpactDetailsRepo impactDetailsRepo;

	@Autowired
	PriorityService priorityService;

	@Autowired
	private AuthUtil authUtil;

	@GetMapping("/fetchAllImpact/{module}")
	public List<ImpactDetailsEntity> getAllImpactList(@PathVariable String module) {
		return impactService.getAllImpactList(module);
	}

	//created By dhiraj for PM Module fetchAllImpact
	@GetMapping("/fetchAllImpact")
	public List<ImpactDetailsEntity> getAllImpactList() {
		return impactService.getAllImpactListForPM();
	}
	

	//created By dhiraj for PM Module fetchAllUrgency
	@GetMapping("/fetchAllUrgency")
	public List<UrgencyDetailsEntity> getAllUrgencyListForPM() {
		return impactService.getAllUrgencyListForPM();
	}

	
	
	@GetMapping("/fetchAllUrgency/{module}")
	public List<UrgencyDetailsEntity> getAllUrgencyList(@PathVariable String module) {
		return impactService.getAllUrgencyList(module);
	}

	@GetMapping("/fetchAllPriority")
	public List<PriorityDetailsEntity> getAllPriority() {
		return priorityService.getAllPriority();
	}

	@GetMapping("/fetchAllPriorities")
	public List<PriorityDetailsEntity> getAllPriorityName() {
		return impactService.getAllDistinctName();
	}

	@PutMapping("/updatePriority/{priorityId}")
	public @Valid PriorityDetailsEntity updatePriority(@RequestHeader("Authorization") String authToken,
			@PathVariable Long priorityId, @Valid @RequestBody PriorityDetailsEntity priority) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_priority)) {
				//logger.info("Updating Priority Class: - " + objMapper.writeValueAsString(priority));
				priority = priorityService.updatePriority(priorityId, priority);
			} else {
				PriorityDetailsEntity pd = new PriorityDetailsEntity();
				pd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return pd;
			}

		} catch (Exception e) {
			logger.error("Exception occur while updating priority " + e.getMessage(),e);

		}
		return priority;
	}

	@GetMapping("checkDupPriority/{impactId}/{urgencyId}/{name}/{module}")
	public PriorityDetailsEntity checkDuplicatePriority(@PathVariable(name = "impactId") Long impactId,
			@PathVariable(name = "urgencyId") Long urgencyId, @PathVariable(name = "name") String name,
			@PathVariable(name = "module") String module) {
		//logger.info("i m in checkDuplicatePriority");
		return priorityService.checkDuplicatePriority(impactId, urgencyId, name, module);
	}

	@GetMapping("getPriorityName/{impactId}/{urgencyId}/{module}")
	public PriorityDetailsEntity getPriorityName(@PathVariable(name = "impactId") Long impactId,
			@PathVariable(name = "urgencyId") Long urgencyId, @PathVariable(name = "module") String module) {

		return priorityService.getPriorityName(impactId, urgencyId, module);
	}

	
	
	//created By dhiraj for PM Module getPriorityName
	
	@GetMapping("getPriorityName/{impactId}/{urgencyId}")
	public PriorityDetailsEntity getPriorityName(@PathVariable(name = "impactId") Long impactId,
			@PathVariable(name = "urgencyId") Long urgencyId) {

		System.out.println("urgencyId==="+urgencyId);
		System.out.println("impactId==="+impactId);
		return priorityService.getPriorityNameForPM(impactId, urgencyId);
	}
	
	@GetMapping("/getPriorityList/{module}")
	public List<PriorityDetailsEntity> getPriorityList(@PathVariable String module) {
		return priorityService.getPriorityList(module);
	}

}
