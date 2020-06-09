package com.tcts.foresight.problemnotification.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.problem.ProblemRcaDetailsEntity;
import com.tcts.foresight.problemnotification.entity.ProblemNotificationCriteriaEntity;
import com.tcts.foresight.problemnotification.serviceImpl.ProblemNotificationCriteriaServiceImpl;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/problemnotificationcriteria")
public class problemNotificationCriteriaController {

	Logger logger = (Logger) LoggerFactory.getLogger(problemNotificationCriteriaController.class);

	@Autowired
	private ProblemNotificationCriteriaServiceImpl lProblemNotificationCriteriaServiceImpl;

	@PostMapping("/rule")
	public ResponseEntity<Object> addNotificationCriteria(@RequestHeader("Authorization") String authToken,@RequestBody ProblemNotificationCriteriaEntity jsonPayload, HttpServletRequest request) {
		ProblemNotificationCriteriaEntity lProblemNotificationCriteriaEntity = null;
		try {
			if (jsonPayload != null) {
				lProblemNotificationCriteriaEntity = lProblemNotificationCriteriaServiceImpl.addnotificationCriteria(jsonPayload);
				if (lProblemNotificationCriteriaEntity != null) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
							.body(lProblemNotificationCriteriaEntity);
				} else {
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
				}

			}
		} catch (Exception e) {
			logger.error("Exception Occurred in addnotificationCriteria: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PutMapping("/rule/{id}")
	public ResponseEntity<Object> updateNotificationCriteria(@RequestHeader("Authorization") String authToken,@RequestBody ProblemNotificationCriteriaEntity jsonPayload, HttpServletRequest request,
			@PathVariable("id") String id) {
		ProblemNotificationCriteriaEntity lProblemNotificationCriteriaEntity = null;
		try {
			if (id != null) {
				if (jsonPayload != null) {
					System.out.println("here");
					lProblemNotificationCriteriaEntity = lProblemNotificationCriteriaServiceImpl.updateNotificationCriteria(jsonPayload);
					if (lProblemNotificationCriteriaEntity != null) {
						return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
								.body(lProblemNotificationCriteriaEntity);
					} else {
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
					}

				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails.BADREQUEST());
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in updateNotificationCriteria: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@GetMapping("/rule")
	public ResponseEntity<Object> fetchAllNotificationCriteria() {
		List<ProblemNotificationCriteriaEntity> lProblemNotificationCriteriaEntityList = new ArrayList<ProblemNotificationCriteriaEntity>();
		try {
			lProblemNotificationCriteriaEntityList = lProblemNotificationCriteriaServiceImpl.fetchAllNotificationCriteria();
			if (lProblemNotificationCriteriaEntityList.size() > 0) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemNotificationCriteriaEntityList);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(lProblemNotificationCriteriaEntityList);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetchAllNotificationCriteria: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
	}
	
	@GetMapping("/rule/{id}")
	public ResponseEntity<Object> fetchAllNotificationCriteriaById(@PathVariable Long id) {
		Optional<ProblemNotificationCriteriaEntity> lProblemNotificationCriteriaEntity = null;
		try {
			lProblemNotificationCriteriaEntity = lProblemNotificationCriteriaServiceImpl.fetchAllNotificationCriteriaById(id);
			if (lProblemNotificationCriteriaEntity !=null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemNotificationCriteriaEntity);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetchAllNotificationCriteriaById: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
	}

	@DeleteMapping("/rule/{id}")
	public ResponseEntity<Object> deletNotificationCriteria(@PathVariable Long id) {
		try {
			if (id != null) {
				lProblemNotificationCriteriaServiceImpl.deletNotificationCriteria(id);
				return ResponseEntity.ok().build();
			}

		} catch (Exception e) {
			logger.error("Exception Occurred while deleting the deletNotificationCriteria");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
	}


}