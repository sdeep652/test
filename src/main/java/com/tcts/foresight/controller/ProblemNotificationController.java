package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.ProblemNotificationEntity;
import com.tcts.foresight.repository.ProblemNotificationRepo;
import com.tcts.foresight.service.impl.ProblemNotificationImpl;
import com.tcts.foresight.util.StringUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/problemnotification")
public class ProblemNotificationController {

	Logger logger = LoggerFactory.getLogger(ProblemNotificationController.class);
	

	@Autowired
	ProblemNotificationImpl problemNotificationImpl;
	
	@Autowired
	ProblemNotificationRepo problemNotificationRepo;
	@PutMapping("/notification")
	public ResponseEntity<Object> removeNotification(@RequestBody ProblemNotificationEntity problemNotificationEntity) {
		//TODO: just take whole entity from UI/from caller. and save as is in DB.
		// make sure UI calls with isEnable=false.
		if (problemNotificationEntity != null) 
		{
			try {
				ProblemNotificationEntity lProblemNotificationEntity =problemNotificationRepo.save(problemNotificationEntity);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemNotificationEntity);
			} catch (Exception e) {
				logger.error("Exception Occurred while removing the notification");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
			}
		}
		return null;
	} 
	
	
	
	@GetMapping("/notification/{emailid}/{module}")
	public ResponseEntity<Object> fetchAllNotificationByEmail(@PathVariable("emailid") String emailid,

			@PathVariable("module") String module) {
		if(StringUtil.isNotNullNotEmpty(emailid)  && StringUtil.isNotNullNotEmpty(module)) {
		try {
			List<ProblemNotificationEntity> notificationEntity = new ArrayList<ProblemNotificationEntity>();

			notificationEntity = problemNotificationImpl.fetchAllNotificationByEmail(emailid, module);
			if (notificationEntity != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(notificationEntity);
			}else {
				logger.error("Exception Occurred while fetch the notification");
				return ResponseEntity.status(HttpStatus.OK).body(new ErrorDetails("200", "Email Id or Module is Missing", "Email Id or Module is Missing"));
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while fetch the notification");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		//return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ErrorDetails("200", "Email Id or Module is Missing", "Email Id or Module is Missing"));
	}
	 
	
	@GetMapping("/notification")
	public ResponseEntity<Object> fetchAllNotification() {

		try {
			List<ProblemNotificationEntity> notificationEntity = new ArrayList<ProblemNotificationEntity>();

			notificationEntity = problemNotificationImpl.fetchAllNotification();
			if (notificationEntity != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(notificationEntity);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while fetch the notification");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}

	return null;

}

//	@PutMapping("/notification")
//	public ResponseEntity<Object> updateNotification(@RequestBody ProblemNotificationEntity notificationEntity) {
//
//		if (!(notificationEntity == null)) {
//			ProblemNotificationEntity lNotificationEntity = new ProblemNotificationEntity();
//			try {
//				lNotificationEntity = problemNotificationImpl.createOrUpdateNotification(notificationEntity);
//				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lNotificationEntity);
//			} catch (Exception e) {
//				logger.error("Exception Occurred while Update the notification");
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//						.body(ErrorDetails.getInternalServerError(e));
//			}
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
//	}

	
	// who ? not used as of now
	@PostMapping("/notification")
	public ResponseEntity<Object> createNotification(@RequestBody ProblemNotificationEntity notificationEntity) {
		System.out.println("notificationEntity ======="+notificationEntity);
		if (notificationEntity != null) {
			ProblemNotificationEntity lNotificationEntity = new ProblemNotificationEntity();
			try {
				lNotificationEntity = problemNotificationImpl.createOrUpdateNotification(notificationEntity);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lNotificationEntity);
			} catch (Exception e) {
				logger.error("Exception Occurred while Create the notification");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ErrorDetails.getInternalServerError(e));
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());

	}
	/*
	 * @GetMapping("notificationbygroupname/{username}/{module}") public
	 * ResponseEntity<Object>
	 * getAllNotificationByGroupName(@PathVariable("username") String username,
	 * 
	 * @PathVariable("module") String module) { try {
	 * List<ProblemNotificationEntity> lProblemNotificationEntity = new
	 * ArrayList<ProblemNotificationEntity>(); lProblemNotificationEntity =
	 * problemNotificationImpl.fetchAllNotificationByEmail(username, module); return
	 * ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
	 * lProblemNotificationEntity); } catch (Exception e) {
	 * logger.error("Exception Occurred in fetch All notification By Group Name: " +
	 * e.getMessage(), e); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.
	 * getInternalServerError(e)); } }
	 */
	 
	
	@GetMapping("/notification/{id}")
	public ResponseEntity<Object> fetchAllNotification(@PathVariable ("id") Long id) {

		try {
			ProblemNotificationEntity notificationEntity = new ProblemNotificationEntity();

			notificationEntity = problemNotificationImpl.fetchAllNotificationById(id);
			if (notificationEntity != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(notificationEntity);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while fetch notification By Id");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}

	return null;

}
	
}
