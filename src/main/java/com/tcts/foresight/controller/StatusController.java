package com.tcts.foresight.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.entity.ResolutionTypeEntity;
import com.tcts.foresight.entity.StatusDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsRemarkEntity;
import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;
import com.tcts.foresight.repository.StatusDetailsRemarkRepo;
import com.tcts.foresight.repository.StatusDetailsRepo;
import com.tcts.foresight.service.StatusService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/status")
public class StatusController {
	private static final Logger logger = LogManager.getLogger(StatusController.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	StatusDetailsRepo statusDetailsRepo;
	@Autowired
	StatusDetailsRemarkRepo statusDetailsRemarkRepo;

	@Autowired
	StatusService statusService;

	@Autowired
	private AuthUtil authUtil;

	@GetMapping("/fetchStatusforRemark")
	public List<StatusDetailsEntity> getStatusforRemark() {
		return statusService.getStatusforRemark();
	}

	@GetMapping("/fetchAllStatus")
	public List<StatusDetailsEntity> getAllStatus() {
		return statusService.getAllStatus();
	}

	@GetMapping("/fetchAllStatusIncident")
	public List<StatusDetailsEntity> getAllStatusIncident() {
		return statusService.getAllStatusIncident();
	}
	@GetMapping("/fetchAllStatusResolutiontype/{module}")
	public List<ResolutionTypeEntity> getAllStatusResolutiontype(@PathVariable String module) {
		return statusService.getAllStatusResolutiontype(module);
	}
	
	@GetMapping("/fetchAllStatusRemark/{module}")
	public List<StatusDetailsRemarkEntity> getAllStatusRemarkList(@PathVariable String module) {
		return statusService.getAllStatusRemarkList(module);
	}

	@PostMapping("/addStatusRemark")
	public @Valid StatusDetailsRemarkEntity addStatusRemark(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody StatusDetailsRemarkEntity status) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_statusremark)) {
				logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(status));
				statusService.addStatusRemark(status);
			} else {
				StatusDetailsRemarkEntity sdr = new StatusDetailsRemarkEntity();
				sdr.setMessage(Constant.User_Does_Not_Have_Authorization);
				return sdr;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in addStatusRemark" + e.getMessage(),e);
		}
		return status;
	}

	@PutMapping(value = "/updateStausRemark/{id}")
	public StatusDetailsRemarkEntity updateStatusRemark(@RequestHeader("Authorization") String authToken,
			@PathVariable Long id, @Valid @RequestBody StatusDetailsRemarkEntity status) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_statusremark)) {
				status = statusService.updateStatusRemark(id, status);
			} else {
				StatusDetailsRemarkEntity sdr = new StatusDetailsRemarkEntity();
				sdr.setMessage(Constant.User_Does_Not_Have_Authorization);
				return sdr;
			}
		} catch (Exception e) {
			logger.error("Exception occur while updating in Category details " + e.getMessage(),e);
		}
		return status;
	}

	@CrossOrigin
	@DeleteMapping(value = "/delete/{id}")
	public String deleteStatusRemark(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_statusremark)) {
				statusService.deleteStatusRemark(id);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in JsonProcessingException " + e.getMessage(),e);
		}

		return "Status deleted successfully";
	}

	@GetMapping("checkDupStatus/{status}/{remark}/{module}")
	public StatusDetailsRemarkEntity checkDuplicateStatus(@PathVariable(name = "status") String status,
			@PathVariable(name = "remark") String remark, @PathVariable(name = "module") String module) {
		return statusService.checkDuplicateStatus(status, remark, module);
	}
	
	@GetMapping("fetchStatusDetails/{status}/{module}")
	public List<StatusDetailsRemarkEntity> fetchStatusDetails(@PathVariable(name = "status") String status,
			@PathVariable(name = "module") String module) {
				return statusService.fetchStatusDetails(status, module);
	}
	
	@GetMapping("getAllUniqueStatus")
	public List<StatusDetailsEntity> getAllUniqueStatus()
	{
		return statusService.getAllUniqueStatus();
	}

	@GetMapping("statusclosecancelled")
	public List<StatusDetailsEntity> getStatusCloseCancelled()
	{
		return statusService.getStatusCloseCancelled();
	}
	
	
	
}
