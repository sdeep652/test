package com.tcts.foresight.controller;

import java.util.List;

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
import com.tcts.foresight.entity.ResolutionMethodEntity;
import com.tcts.foresight.service.ResolutionMethodService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/resolutionMet")
public class ResolutionMethodController {

	private static final Logger logger = LogManager.getLogger(ResolutionMethodController.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	ResolutionMethodService resolutionMetService;

	@Autowired
	private AuthUtil authUtil;

	@GetMapping("/fetchAllResolutionMethod/{module}")
	public List<ResolutionMethodEntity> getAllResolutionMethod(@PathVariable String module) {
		return resolutionMetService.getAllResolutionMethod(module);
	}

	@PostMapping("/addResolutionMethod")
	public @Valid ResolutionMethodEntity addResolutionMethod(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody ResolutionMethodEntity resolution) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_resolutionmethod)) {
				logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(resolution));
				resolutionMetService.addResolutionMethod(resolution);
			} else {
				ResolutionMethodEntity rd = new ResolutionMethodEntity();
				rd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return rd;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in add Resolution Method " + e.getMessage(),e);
		}
		return resolution;
	}

	@PutMapping(value = "/updateResolutionMethod/{id}")
	public ResolutionMethodEntity updateResolutionMethod(@RequestHeader("Authorization") String authToken,
			@PathVariable Long id, @Valid @RequestBody ResolutionMethodEntity resolution) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_resolutionmethod)) {
				resolution = resolutionMetService.updateResolutionMethod(id, resolution);
			} else {
				ResolutionMethodEntity rd = new ResolutionMethodEntity();
				rd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return rd;
			}
		} catch (Exception e) {
			logger.error("Exception occur while updating in Resolution Method " + e.getMessage(),e);
		}
		return resolution;
	}

	@CrossOrigin
	@DeleteMapping(value = "/delete/{id}")
	public String deleteResolutionMethod(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_resolutionmethod)) {
				resolutionMetService.deleteResolutionMethod(id);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in JsonProcessingException " + e.getMessage(),e);
		}
		return "Resolution Method Deleted Successfully";
	}

	@GetMapping("checkDupResolution/{name}/{module}")
	public ResolutionMethodEntity checkDuplicateResolutionMethod(@PathVariable(name = "name") String name,
			@PathVariable(name = "module") String module) {
		return resolutionMetService.checkDuplicateResolutionMethod(name, module);
	}

}
