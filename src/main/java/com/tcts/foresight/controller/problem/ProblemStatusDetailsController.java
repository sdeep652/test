package com.tcts.foresight.controller.problem;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;
import com.tcts.foresight.service.impl.problem.ProblemStatusDetailsImpl;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/problem/status")
public class ProblemStatusDetailsController {

	Logger logger = (Logger) LoggerFactory.getLogger(ProblemStatusDetailsController.class);

	@Autowired
	ProblemStatusDetailsImpl problemStatusDetailsImpl;

	@GetMapping("/allstatus")
	public List<ProblemStatusDetailsEntity> getAllStatus() {
		List<ProblemStatusDetailsEntity> lProblemStatusDetailsEntity = null;
		try {
			lProblemStatusDetailsEntity = problemStatusDetailsImpl.getAllStatus();
		} catch (Exception e) {
			logger.error("Exception Occurred in getAllStatus: " + e.getMessage(), e);
		}
		return lProblemStatusDetailsEntity;
	}

	@GetMapping("/filterstatus")
	public List<ProblemStatusDetailsEntity> getFilterStatus() {
		List<ProblemStatusDetailsEntity> lProblemStatusDetailsEntity = null;
		try {
			lProblemStatusDetailsEntity = problemStatusDetailsImpl.getFilterStatus();
		} catch (Exception e) {
			logger.error("Exception Occurred in getFilterStatus: " + e.getMessage(), e);
		}
		return lProblemStatusDetailsEntity;
	}

	@GetMapping("/updateproblemstatus")
	public List<ProblemStatusDetailsEntity> getUpdateProbleStatus() {
		List<ProblemStatusDetailsEntity> lProblemStatusDetailsEntity = null;
		try {
			lProblemStatusDetailsEntity = problemStatusDetailsImpl.getUpdateProbleStatus();
		} catch (Exception e) {
			logger.error("Exception Occurred in getUpdateProbleStatus: " + e.getMessage(), e);
		}
		return lProblemStatusDetailsEntity;
	}

}
