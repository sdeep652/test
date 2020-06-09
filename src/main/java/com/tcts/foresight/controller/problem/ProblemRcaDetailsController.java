package com.tcts.foresight.controller.problem;

import java.util.ArrayList;
import java.util.List;

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
import com.tcts.foresight.service.impl.problem.ProblemRcaDetailsImpl;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/problemresolution")
public class ProblemRcaDetailsController {

	Logger logger = (Logger) LoggerFactory.getLogger(ProblemRcaDetailsController.class);

	@Autowired
	ProblemRcaDetailsImpl problemRcaDetailsImpl;

	@PostMapping("/rca")
	public ResponseEntity<Object> addProblemRca(@RequestHeader("Authorization") String authToken,@RequestBody ProblemRcaDetailsEntity jsonPayload, HttpServletRequest request) {
		ProblemRcaDetailsEntity problemRcaDetailsEntity = null;
		try {
			if (jsonPayload != null) {
				problemRcaDetailsEntity = problemRcaDetailsImpl.addProblemRca(jsonPayload);
				if (problemRcaDetailsEntity != null) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(problemRcaDetailsEntity);
				} else {
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
				}

			}
		} catch (Exception e) {
			logger.error("Exception Occurred in addProblemRca: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PutMapping("/rca/{id}")
	public ResponseEntity<Object> updateProblemRca(@RequestHeader("Authorization") String authToken,@RequestBody ProblemRcaDetailsEntity jsonPayload, HttpServletRequest request,
			@PathVariable("id") String id) {
		ProblemRcaDetailsEntity problemRcaDetailsEntity = null;
		try {
			if (id != null) {
				if (jsonPayload != null) {
					problemRcaDetailsEntity = problemRcaDetailsImpl.updateProblemRca(jsonPayload);
					if (problemRcaDetailsEntity != null) {
						return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
								.body(problemRcaDetailsEntity);
					} else {
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
					}

				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails.BADREQUEST());
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in updateProblemRca: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@GetMapping("/rca")
	public ResponseEntity<Object> fetchAllProblemRca() {
		List<ProblemRcaDetailsEntity> problemRcaDetailsEntity = new ArrayList<ProblemRcaDetailsEntity>();
		try {
			problemRcaDetailsEntity = problemRcaDetailsImpl.fetchAllProblemRca();
			if (problemRcaDetailsEntity.size() > 0) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(problemRcaDetailsEntity);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetchAllProblemRca: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
	}

	@DeleteMapping("/rca/{id}")
	public ResponseEntity<Object> deleteAttachment(@PathVariable Long id) {
		try {
			if (id != null) {
				problemRcaDetailsImpl.DeleteProblemRca(id);
				return ResponseEntity.ok().build();
			}

		} catch (Exception e) {
			logger.error("Exception Occurred while deleting the rca");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
	}

}
