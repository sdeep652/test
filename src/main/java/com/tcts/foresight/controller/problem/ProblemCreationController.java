package com.tcts.foresight.controller.problem;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.IncidentStatusEntity;
import com.tcts.foresight.entity.ProblemNotificationEntity;
import com.tcts.foresight.entity.problem.ProblemAttachmentEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemHistoryEntity;
import com.tcts.foresight.entity.problem.ProblemIncidentEntity;
import com.tcts.foresight.entity.problem.ProblemStatusEntity;
import com.tcts.foresight.exception.CustomValidationException;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.repository.CSATFeedbackRepo;
import com.tcts.foresight.repository.problem.ProblemDetailsRepo;
import com.tcts.foresight.repository.problem.ProblemIncidentRepo;
import com.tcts.foresight.service.JbpmProcessService;
import com.tcts.foresight.service.impl.ParentChildServiceImpl;
import com.tcts.foresight.service.impl.ProblemNotificationImpl;
import com.tcts.foresight.service.impl.problem.ProblemManagementImpl;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.ProblemRequestValidationUtil;
import com.tcts.foresight.util.StringUtil;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/problem")
public class ProblemCreationController {
	
	Logger logger = (Logger) LoggerFactory.getLogger(ProblemCreationController.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	CSATFeedbackRepo lCSATRepo;

	@Autowired
	private AuthUtil authUtil;

	@Autowired
	private ProblemManagementImpl lProblemManagementImpl;
	
	@Autowired
	private JbpmProcessService jbpmProcessService;
	
	@Autowired
	ProblemManagementImpl problemManagementImpl;
	
	@Autowired
	ProblemRequestValidationUtil problemRequestValidationUtil;
	
	@Autowired
	ProblemIncidentRepo problemIncidentRepo;
	
	@Autowired
	ProblemDetailsRepo problemDetailsRepo;
	
	@Autowired
	ProblemNotificationImpl problemNotificationImpl;
	
	@Autowired
	ParentChildServiceImpl parentChildServiceImpl;
	
	@PostMapping("problemmanagement")
	public ResponseEntity<Object> createProblem(@RequestHeader("Authorization") String authToken, @RequestBody ProblemDetailsEntity jsonPayload, HttpServletRequest request)
			throws ResourceNotFoundException, CustomValidationException {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.PM_Page_problemcreation)) {
				
				ErrorDetails ed = problemRequestValidationUtil.createProblemRequest(authToken,jsonPayload);
				
				if(ed == null)
				{
					ProblemDetailsEntity lProblemDetailsEntity = null;
					List<ProblemAttachmentEntity> currentAttachementList = new ArrayList<ProblemAttachmentEntity>();
					if(jsonPayload.getProblemAttachmentList() !=null) {
					currentAttachementList.addAll(jsonPayload.getProblemAttachmentList());
					System.out.println("currentAttachementList ======= "+currentAttachementList);
					jsonPayload.setProblemAttachmentList(null);
					}
					String processInstancePayload  = JSONUtil.objectToJson(jsonPayload);
					String processInstanceId = jbpmProcessService.createProcessInstance(authToken, Constant.CONTAINER_ID, Constant.PROCESS_ID, Constant.CLIENT, processInstancePayload);
					if(StringUtil.isNotNullNotEmpty(processInstanceId))
					{
						String taskId = jbpmProcessService.getOnlyTaskId(authToken, processInstanceId);
						
						if(StringUtil.isNotNullNotEmpty(taskId))
						{
							if(currentAttachementList !=null) {
							jsonPayload.setProblemAttachmentList(currentAttachementList);
							currentAttachementList = null;
							}
							lProblemDetailsEntity = lProblemManagementImpl.completeTask(authToken, Constant.CONTAINER_ID,taskId, Constant.CLIENT, processInstanceId, jsonPayload);
							if(lProblemDetailsEntity!=null && StringUtil.isNotNullNotEmpty(lProblemDetailsEntity.getProblemID()))
							{
								URI uri = new URI(request.getRequestURL().toString()+"/"+lProblemDetailsEntity.getProblemID());
								return ResponseEntity.created(uri).contentType(MediaType.APPLICATION_JSON).body(lProblemDetailsEntity);
							}
							
						}
					}
				}
				else {
					return ResponseEntity.status(HttpStatus.OK).body(ed);
				}
								
			} else {
//				return new ResponseEntity(new ErrorDetails("401", Constant.User_Does_Not_Have_Authorization, Constant.User_Does_Not_Have_Authorization), HttpStatus.UNAUTHORIZED);
					return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in createProblem: " + e.getMessage(), e);
//			return new ResponseEntity(new ErrorDetails("500", "Internal Server Error", "Some internal error occurred, please try after some time. "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@GetMapping("problemmanagement/{problemId}")
	public ResponseEntity<Object> getProblemDetailsById(@RequestHeader("Authorization") String authToken, @PathVariable("problemId") String problemId){
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.PM_Page_problemcreation)) {
				ProblemDetailsEntity lProblemDetailsEntity = null;
				if(StringUtil.isNotNullNotEmpty(problemId))
				{
					lProblemDetailsEntity = lProblemManagementImpl.getProblemDetailsById(problemId);
					
						if(lProblemDetailsEntity!=null )
						{
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemDetailsEntity);
						}else {
							return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
						}
				}
				
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getProblemDetailsById: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PutMapping("problemmanagement/{problemId}")
	public ResponseEntity<Object> updateProblem(@RequestHeader("Authorization") String authToken, @PathVariable("problemId") String problemID, @RequestBody ProblemDetailsEntity jsonPayload)
	{
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.PM_Page_problemupdate,jsonPayload.getProblemID()) ) {
				
				ErrorDetails ed = problemRequestValidationUtil.UpdateProblemRequest(authToken, problemID, jsonPayload);
				
				if(ed == null){
					if(StringUtil.isNotNullNotEmpty(jsonPayload.getProcessInstanceId()))
					{
						String taskId = jbpmProcessService.getOnlyTaskId(authToken, jsonPayload.getProcessInstanceId());
						
						if(StringUtil.isNotNullNotEmpty(taskId))
						{
							ProblemDetailsEntity lProblemDetailsEntity  = lProblemManagementImpl.completeTask(authToken, Constant.CONTAINER_ID,taskId, Constant.CLIENT, jsonPayload.getProcessInstanceId(), jsonPayload);
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemDetailsEntity);
						}
					}
				} else {
					return ResponseEntity.status(HttpStatus.OK).body(ed);
				}
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PatchMapping("problemmanagement/{problemId}")
	public ResponseEntity<Object> patchIncident(@RequestHeader("Authorization") String authToken, @PathVariable("problemId") String problemId, @RequestBody HashMap<String, Object> jsonPayload)
	{
		System.out.println("inside patch");
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.PM_Page_problemcreation)) {
				
				ProblemDetailsEntity lProblemDetailsEntity = lProblemManagementImpl.patchCheckAndUpdate(problemId,jsonPayload);
				
//				if(lProblemDetailsEntity!=null)
//				{
//					if(StringUtil.isNotNullNotEmpty(lProblemDetailsEntity.getProcessInstanceId()))
//					{
//						String taskId = jbpmProcessService.getOnlyTaskId(authToken, lProblemDetailsEntity.getProcessInstanceId());
//						if(StringUtil.isNotNullNotEmpty(taskId))
//						{
//							lProblemDetailsEntity = lProblemManagementImpl.completeTask(authToken, Constant.CONTAINER_ID,taskId, Constant.CLIENT, lMappedVariableInstanceLogVO.getProcessInstanceId(), lMappedVariableInstanceLogVO);
//							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lProblemDetailsEntity);
//						}
//					}
//				}
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in patchIncident: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	/*
	 * @PostMapping("/attachment") public List<ProblemAttachmentEntity>
	 * addAttachment(
	 * 
	 * @RequestBody List<ProblemAttachmentEntity> problemAttachmentList) { return
	 * problemManagementImpl.addAttachment(problemAttachmentList); }
	 */

	@GetMapping("/attachment/{problemid}")
	public ResponseEntity<Object> fetchProblemAttchmentList(@PathVariable String problemid) {

		try {
			List<ProblemAttachmentEntity> problemAttachmentEntity = null;
			if (StringUtil.isNotNullNotEmpty(problemid)) {

				problemAttachmentEntity = lProblemManagementImpl.fetchProblemAttchmentList(problemid);
				if (problemAttachmentEntity != null) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(problemAttachmentEntity);
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetching the attachment");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}

	@DeleteMapping("/attachment/{attachmentid}")
	public ResponseEntity<Object> deleteAttachment(@PathVariable Long attachmentid) {
		try {
			if (attachmentid != null) {
				problemManagementImpl.deleteAttachment(attachmentid);
				return ResponseEntity.ok().build();
			}

		} catch (Exception e) {
			logger.error("Exception Occurred while deleting the attachment");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
	}
	
	@GetMapping("/relateditems/{problemid}")
	public ResponseEntity<Object> fetchProblemAssociatedIncidents(@PathVariable String problemid) {
		try {
			List<ProblemIncidentEntity> problemIncidentEntity = new ArrayList<ProblemIncidentEntity>();
			if (StringUtil.isNotNullNotEmpty(problemid)) {
				problemIncidentEntity = lProblemManagementImpl.fetchProblemAssociatedIncidents(problemid);
				if (problemIncidentEntity != null) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(problemIncidentEntity);
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetching the Associated Incident List");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}

	@GetMapping("/relateditems/{problemid}/{relationtype}")
	public ResponseEntity<Object> fetchProblemAssociatedIncidentsByRelationType(@PathVariable String problemid,
			@PathVariable String relationtype) {

		try {
			List<ProblemIncidentEntity> problemIncidentEntity = new ArrayList<ProblemIncidentEntity>();
			if (StringUtil.isNotNullNotEmpty(problemid) && StringUtil.isNotNullNotEmpty(relationtype)) {

				problemIncidentEntity = lProblemManagementImpl.fetchProblemAssociatedIncidentsByRelationType(problemid,
						relationtype);
				if (problemIncidentEntity != null) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(problemIncidentEntity);
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetching the Associated Incident List");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}

	@DeleteMapping("/relateditems/{id}")
	public ResponseEntity<Object> deleteProblemAssociatedIncidents(@PathVariable Long id) {
		try {
			if (id != null) {
				problemManagementImpl.deleteProblemAssociatedIncidents(id);
				return ResponseEntity.ok().build();
			}

		} catch (Exception e) {
			logger.error("Exception Occurred while deleting the Associated Incidents");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
	}

	@PostMapping("/problemmanagement/problemlist")
	public List<ProblemDetailsEntity> problemList(@RequestHeader("Authorization") String authToken, @RequestBody String jsonPayLoadMap) {
		// logger.info("inside filtered job");
		return lProblemManagementImpl.problemList(jsonPayLoadMap);
	}
	
	@GetMapping("/problemHistory/{problemID}")
	public List<ProblemHistoryEntity> fetchAllProblemHistory(@PathVariable String problemID) {
		return lProblemManagementImpl.fetchAllProblemHistory(problemID);
	}
	
	@PostMapping("updateprocessinstancedetails")
	public boolean updateProcessInstanceDetails(@RequestBody String jsonPayload) {
		boolean isUpdateSuccessfull = false;
		try {
			if (jsonPayload != null && jsonPayload.trim().length() > 0) {
				JSONObject jsonPayloadRequest = new JSONObject(jsonPayload);
				if (jsonPayloadRequest != null && jsonPayloadRequest.has("processinstanceid")) {
					String processInstanceId = jsonPayloadRequest.get("processinstanceid").toString();
					if (processInstanceId != null) {
						ProblemDetailsEntity problemDetailsEntity = problemManagementImpl
								.checkAndInsertInAuxDB(processInstanceId);
						if (problemDetailsEntity != null) {
							isUpdateSuccessfull = true;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in updateProcessInstanceDetails: " + e.getMessage(), e);
		}
		return isUpdateSuccessfull;
	}

	@PutMapping("assignToMe/{containerId}")
	public ResponseEntity<Object> assignToMe(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @RequestParam("client") String clientCode,
			@RequestBody List<ProblemDetailsEntity> jsonPayload) {
		HashMap<String, String> returnMap = null;
		try {
			if (containerId.equals(Constant.CONTAINER_ID)) {
				returnMap = lProblemManagementImpl.assignToMe(authToken, containerId, jsonPayload, clientCode);
				if (!(returnMap == null)) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(returnMap);
				}
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ErrorDetails("200", "CONTAINER ID is wrong ", "CONTAINER ID is wrong "));
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in Assign To Me" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());

	}
	
	
	//For Problem 
		@GetMapping("/fetchStatusVisited/{problemId}")
		public HashMap<String, List<String>> fetchStatusVisited(@PathVariable String problemId) {
			return problemManagementImpl.fetchStatusVisited(problemId);
		}
		
	/*
	 * @GetMapping("notification") public ResponseEntity<Object>
	 * getAllNotification(){ try { List<ProblemNotificationEntity>
	 * lProblemNotificationEntity = new ArrayList<ProblemNotificationEntity>();
	 * lProblemNotificationEntity =
	 * problemNotificationImpl.fetchAllEnableNotification(); return
	 * ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
	 * lProblemNotificationEntity); } catch (Exception e) {
	 * logger.error("Exception Occurred in fetch All notification: " +
	 * e.getMessage(), e); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.
	 * getInternalServerError(e)); } // return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.
	 * getInternalServerError()); }
	 */
		
		@PostMapping("/fetchallincidentistbasedongroup")
		public ResponseEntity<Object> fetchallIncidentList(@RequestHeader("Authorization") String authToken,
				@RequestBody String jsonPayLoadMap) {
			List<ProblemStatusEntity> lProblemStatusEntity = new ArrayList<ProblemStatusEntity>();
			HashMap<String, String> filter = new HashMap<String, String>();

			try {
				if (StringUtil.isNotNullNotEmpty(jsonPayLoadMap)) {
					filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
					if (filter != null && filter.size() > 0) {
						if (StringUtil.isNotNullNotEmpty(filter.get("assignmentGroup"))
								&& filter.containsKey("assignmentGroup")) {
							lProblemStatusEntity = parentChildServiceImpl.getIncidentListForProblem(jsonPayLoadMap);
							if (lProblemStatusEntity != null) {
								return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
										.body(lProblemStatusEntity);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception Occurred while fetchallIncidentList");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails.BADREQUEST());
		}
}

