package com.tcts.foresight.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.IncidentAttachmentEntity;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.IncidentStatusEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.exception.CustomValidationException;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.repository.CSATFeedbackRepo;
import com.tcts.foresight.service.IncidentCreationService;
import com.tcts.foresight.service.JbpmProcessService;
import com.tcts.foresight.service.impl.IncidentCreationServiceImpl;
import com.tcts.foresight.service.impl.IncidentNotificationImpl;
import com.tcts.foresight.service.impl.ParentChildServiceImpl;
import com.tcts.foresight.service.impl.ProblemNotificationImpl;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.IncidentRequestValidation;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.StringUtil;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/incident")
public class IncidentCreationController {
	Logger logger = (Logger) LoggerFactory.getLogger(IncidentCreationController.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	CSATFeedbackRepo lCSATRepo;

	@Autowired
	private AuthUtil authUtil;

	@Autowired
	private IncidentCreationService incidentCreationService;

	@Autowired
	private IncidentCreationServiceImpl incidentCreationServiceImpl;
	
	@Autowired
	ProblemNotificationImpl problemNotificationImpl;
	
	@Autowired
	private JbpmProcessService jbpmProcessService;
	
	@Autowired
	ParentChildServiceImpl parentChildServiceImpl;
	
	@Autowired
	IncidentRequestValidation incidentRequestValidation;
	
	@Autowired
	private ProblemNotificationImpl lProblemNotificationImpl;
	
	@Autowired
	ConfigEntityCached lConfigEntityCached;
	
	@Autowired
	IncidentNotificationImpl lIncidentNotificationImpl;
	
//Orginial API for PUT
	
	/*
	 * @PutMapping("instances/complete/{containerId}/{taskInstanceId}") public
	 * String completeTask(@RequestHeader("Authorization") String authToken,
	 * 
	 * @PathVariable("containerId") String
	 * containerId, @PathVariable("taskInstanceId") String taskInstanceId,
	 * 
	 * @RequestParam("client") String clientCode, @RequestParam("processInstanceId")
	 * String processInstanceId,
	 * 
	 * @RequestBody MappedVariableInstanceLogVO jsonPayload) throws
	 * ResourceNotFoundException, CustomValidationException { String returnString =
	 * null; try { if (authUtil.authenticateAndAuthorizeCheck(authToken,
	 * Constant.IM_Page_incidentcreation)) {
	 * 
	 * MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
	 * lMappedVariableInstanceLogVO =
	 * incidentCreationService.completeTask(authToken, containerId,taskInstanceId,
	 * clientCode, processInstanceId, jsonPayload); returnString =
	 * objMapper.writeValueAsString(lMappedVariableInstanceLogVO); } else { return
	 * Constant.User_Does_Not_Have_Authorization; } } catch (JsonProcessingException
	 * e) { logger.error("Exception Occurred in completeTask: " + e.getMessage(),
	 * e); } return returnString; }
	 */
	
	
	
	@PutMapping("instances/complete/{containerId}/{taskInstanceId}")
	public ResponseEntity<Object> completeTask(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @PathVariable("taskInstanceId") String taskInstanceId,
			@RequestParam("client") String clientCode, @RequestParam("processInstanceId") String processInstanceId,
			@RequestBody MappedVariableInstanceLogVO jsonPayload)
			throws ResourceNotFoundException, CustomValidationException {
		try {
			if (jsonPayload.getIncidentID() == null) {

				if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {
					ErrorDetails ed = null;
					ed = incidentRequestValidation.createIncidentRequest(authToken, jsonPayload);
					String returnString = null;
					MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
					if (ed == null) {
						lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken, containerId,
								taskInstanceId, clientCode, processInstanceId, jsonPayload);
						returnString = objMapper.writeValueAsString(lMappedVariableInstanceLogVO);
						return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(returnString);

					} else {
						return ResponseEntity.status(HttpStatus.OK).body(ed);
					}
				} else {
					return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
				}
			} else {
				if (authUtil.authenticateAndAuthorizeCheckForIncidentUpdate(authToken, Constant.IM_Page_incidentupdate,jsonPayload.getIncidentID())) {
					ErrorDetails ed = null;
					ed = incidentRequestValidation.UpdateIncidentRequest(authToken, jsonPayload.getIncidentID(), jsonPayload);
					String returnString = null;
					MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
					if (ed == null) {
						lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken, containerId,
								taskInstanceId, clientCode, processInstanceId, jsonPayload);
						returnString = objMapper.writeValueAsString(lMappedVariableInstanceLogVO);
						return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(returnString);

					} else {
						return ResponseEntity.status(HttpStatus.OK).body(ed);
					}
				} else {
					return ResponseEntity.status(HttpStatus.OK).body(ErrorDetails.getUnAuthorized());
				}
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		//return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	
	
	
	@GetMapping("/filteredjoblist1/{userName}")
	public List<MappedVariableInstanceLogVO> filteredJobList1(@PathVariable String userName) {
		return incidentCreationService.filteredJobList1(userName);
	}

	@PostMapping("/filteredjoblist")
	public List<MappedVariableInstanceLogVO> filteredJobList(@RequestBody String jsonPayLoadMap) {
		// logger.info("inside filtered job");
		return incidentCreationService.filteredJobList(jsonPayLoadMap);
	}

//	@PostMapping("/filteredjoblistWithGroupVisited")
//	public List<MappedVariableInstanceLogVO> filteredjoblistWithGroupVisited(@RequestBody String jsonPayLoadMap) {
//		// logger.info("inside filtered job group visited");
//		return incidentCreationService.filteredjoblistWithGroupVisited(jsonPayLoadMap);
//	}

	@PutMapping("assignToMe/{containerId}")
	public HashMap<String, String> assignToMe(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @RequestParam("client") String clientCode,
			@RequestBody List<MappedVariableInstanceLogVO> jsonPayload) {
		// logger.info("inside assignToMe");
		HashMap<String, String> returnMap = null;
		returnMap = incidentCreationService.assignToMe(authToken, containerId, jsonPayload, clientCode);

		return returnMap;
	}

	@PostMapping("/filteredjoblistpage")
	public List<MappedVariableInstanceLogVO> filteredJobListByPagination(@RequestBody String jsonPayLoadMap,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "processInstanceId") String sortBy) {
		// logger.info("inside page list job");
		List<MappedVariableInstanceLogVO> list = incidentCreationService.filteredJobListByPagination(pageNo, pageSize,
				sortBy, jsonPayLoadMap);
		return list;
	}

	@GetMapping("/incidentList/{incidentID}")
	public List<MappedVariableInstanceLogVO> fetchAllIncidentList(@PathVariable String incidentID) {
		return incidentCreationService.fetchAllIncidentList(incidentID);
	}

	@PutMapping("resolveIncidentsTT/{containerId}")
	public HashMap<String, String> resolveIncidentTT(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @RequestParam("client") String clientCode,
			@RequestBody List<MappedVariableInstanceLogVO> jsonPayload) {

		HashMap<String, String> lJSONObject = null;
		try {
			logger.info("Bulk Incidents TT to Resolve:- " + objMapper.writeValueAsString(jsonPayload));
			lJSONObject = incidentCreationService.resolveIncidentTT(authToken, containerId, clientCode, jsonPayload);
			// logger.info("Bulk Incidents Resolved TT List:- " + lJSONObject.toString());

		} catch (JsonProcessingException e) {
			logger.error("Exception Occurred in resolveIncidentTT:" + e.getMessage(), e);
		}
		return lJSONObject;
	}

	// Incident History
	@PutMapping("incidentHistory/{ticketId}")
	public String completeTaskNewGanesh(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @PathVariable("taskInstanceId") String taskInstanceId,
			@RequestParam("client") String clientCode, @RequestParam("processInstanceId") String processInstanceId,
			@RequestBody MappedVariableInstanceLogVO jsonPayload)
			throws ResourceNotFoundException, CustomValidationException {
		String returnString = null;
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {

				MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
				lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken, containerId,
						taskInstanceId, clientCode, processInstanceId, jsonPayload);
				returnString = objMapper.writeValueAsString(lMappedVariableInstanceLogVO);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception Occurred in completeTaskNewGanesh: " + e.getMessage(), e);
		}
		return returnString;
	}

	@GetMapping("/incidentHistory/{incidentID}")
	public List<IncidentHistoryEntity> fetchAllIncidentHistory(@PathVariable String incidentID) {
		return incidentCreationService.fetchAllIncidentHistory(incidentID);
	}
	
//	@GetMapping("/problemNotification")
//	public List<MappedVariableInstanceLogVO> checkAndCreateProblemNotification() {
//		MappedVariableInstanceLogVO lMappedVariableInstanceLogVO =new MappedVariableInstanceLogVO();
//		lMappedVariableInstanceLogVO.setIncidentID("IN005");
//		return lProblemNotificationImpl.checkAndCreateProblemNotification( lMappedVariableInstanceLogVO);
//	}


	@PostMapping("updateProcessInstanceDetails")
	public boolean updateProcessInstanceDetails(@RequestBody String jsonPayload) {

		boolean isUpdateSuccessfull = false;
		try {
			if (jsonPayload != null && jsonPayload.trim().length() > 0) {
				JSONObject jsonPayloadRequest = new JSONObject(jsonPayload);
				if (jsonPayloadRequest != null && jsonPayloadRequest.has("processinstanceid")) {
					String processInstanceId = jsonPayloadRequest.get("processinstanceid").toString();
					if (processInstanceId != null) {
						// logger.info("processinstanceid: " + processInstanceId);
						MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = incidentCreationServiceImpl.checkAndInsertInAuxDBVaibhav(processInstanceId);

						isUpdateSuccessfull = true; 
						problemNotificationImpl.notificationForProblem(lMappedVariableInstanceLogVO);
                     	Thread t = new Thread() {
							public void run() {
								if(lMappedVariableInstanceLogVO.getFeedbackCheckbox()==null) {
									lMappedVariableInstanceLogVO.setFeedbackCheckbox("false");
									}
								if (lMappedVariableInstanceLogVO != null && lMappedVariableInstanceLogVO.getFeedbackCheckbox().equalsIgnoreCase("true")) {
									incidentCreationService.sendLink(lMappedVariableInstanceLogVO);
								}
								if(lConfigEntityCached.getValue("email.incident.closure.created.by.notification").equalsIgnoreCase("Yes")) {
									lIncidentNotificationImpl.closedNotificationToCreator(lMappedVariableInstanceLogVO);
								}
								lIncidentNotificationImpl.statusChangedNotification(lMappedVariableInstanceLogVO,false);
							}
						};
						t.start(); 
 

					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in updateProcessInstanceDetails: " + e.getMessage(), e);
		}
		return isUpdateSuccessfull;
	}

	@PostMapping("/addAttachment")
	public List<IncidentAttachmentEntity> addAttachment(
			@RequestBody List<IncidentAttachmentEntity> incidentAttachmentList) {
		return incidentCreationService.addAttachment(incidentAttachmentList);
	}

	@GetMapping("/fetchIncidentAttchmentList/{ticketId}")
	public List<IncidentAttachmentEntity> fetchIncidentAttchmentList(@PathVariable String ticketId) {
		return incidentCreationService.fetchIncidentAttchmentList(ticketId);
	}

	@GetMapping("/fetchStatusVisited/{ticketId}")
	public HashMap<String, List<String>> fetchStatusVisited(@PathVariable String ticketId) {
		return incidentCreationService.fetchStatusVisited(ticketId);
	}

	@DeleteMapping("/deleteAttachment/{attachmentId}")
	public void deleteAttachment(@PathVariable Long attachmentId, @RequestHeader("Authorization") String authToken ) {
		incidentCreationService.deleteAttachment(attachmentId,authToken);
	}

	// Karan
	@PostMapping("/getParentId")
	public List<String> getIncidentId(@RequestBody List<String> status) {
		return incidentCreationService.getIncidentId(status);

	}

	@PostMapping("/fechAllRecords{module}")
	public List<String> fechAllRecords(@RequestBody List<String> status) {
		return incidentCreationService.getIncidentId(status);

	}

	@GetMapping("/fechAllIncident/{module}")
	public List<IncidentStatusEntity> fechAllRecords(@PathVariable String module) {
	
		List<String> list = incidentCreationService.fechAllRecords(module, Constant.INCIDENT_STATUS_CLOSED, Constant.INCIDENT_STATUS_CANCELLED);
		List<IncidentStatusEntity> IncidentStatusEntityList = new ArrayList<IncidentStatusEntity>();
		for (String str : list) {
			String[] splitedString = str.split(",");
			String processinstanceId = splitedString[0];
			String incidentId = splitedString[1];
			IncidentStatusEntity lIncidentStatusEntity = new IncidentStatusEntity();
			lIncidentStatusEntity.setIncidentId(incidentId);
			lIncidentStatusEntity.setProcessInstanceId(processinstanceId);
			IncidentStatusEntityList.add(lIncidentStatusEntity);
		}
		return IncidentStatusEntityList;
	}

	
	
	@PostMapping("/incidentClone/{containerId}/{processId}/{incidentId}")
	public MappedVariableInstanceLogVO incidentClone(@RequestHeader("Authorization") String authToken,
			@PathVariable String incidentId, @PathVariable("containerId") String containerId,
			@PathVariable("processId") String processId,@RequestParam("client") String clientCode ,String requestPayload) {
		return incidentCreationService.incidentClone(authToken, containerId,processId, clientCode, requestPayload, incidentId);
	
	}
	

	
	@PostMapping("incidentmanagement")
	public ResponseEntity<Object> createIncident(@RequestHeader("Authorization") String authToken, @RequestBody MappedVariableInstanceLogVO jsonPayload, HttpServletRequest request)
			throws ResourceNotFoundException, CustomValidationException {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {

				ErrorDetails ed = incidentRequestValidation.createIncidentRequest(authToken, jsonPayload);
				
				if(ed == null) {
				MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
			
				String processInstancePayload  = JSONUtil.objectToJson(jsonPayload);
				
				String processInstanceId = jbpmProcessService.createProcessInstance(authToken, Constant.CONTAINER_ID, Constant.PROCESS_ID, Constant.CLIENT, processInstancePayload);
				
				if(StringUtil.isNotNullNotEmpty(processInstanceId))
				{
					String taskId = jbpmProcessService.getOnlyTaskId(authToken, processInstanceId);
					
					if(StringUtil.isNotNullNotEmpty(taskId))
					{
						lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken, Constant.CONTAINER_ID,taskId, Constant.CLIENT, processInstanceId, jsonPayload);
						URI uri = new URI(request.getRequestURL().toString()+"/"+lMappedVariableInstanceLogVO.getIncidentID());
						return ResponseEntity.created(uri).contentType(MediaType.APPLICATION_JSON).body(lMappedVariableInstanceLogVO);
					}
				}
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ed);
			}
			} else {
//				return new ResponseEntity(new ErrorDetails("401", Constant.User_Does_Not_Have_Authorization, Constant.User_Does_Not_Have_Authorization), HttpStatus.UNAUTHORIZED);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
//			return new ResponseEntity(new ErrorDetails("500", "Internal Server Error", "Some internal error occurred, please try after some time. "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@GetMapping("incidentmanagement/{incidentId}")
	public ResponseEntity<Object> getIncidentDetailsById(@RequestHeader("Authorization") String authToken, @PathVariable("incidentId") String incidentId){
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {
				MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
				if(StringUtil.isNotNullNotEmpty(incidentId))
				{
					lMappedVariableInstanceLogVO = incidentCreationServiceImpl.getIncidentDetailsById(incidentId);
					
						if(lMappedVariableInstanceLogVO!=null )
						{
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lMappedVariableInstanceLogVO);
						}
				}
				
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PutMapping("incidentmanagement/{incidentId}")
	public ResponseEntity<Object> putIncident(@RequestHeader("Authorization") String authToken,
			@PathVariable("incidentId") String incidentId, @RequestBody MappedVariableInstanceLogVO jsonPayload) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {
				ErrorDetails ed = incidentRequestValidation.UpdateIncidentRequest(authToken, incidentId, jsonPayload);

				MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
				if (ed == null) {

//				String processInstanceId = jbpmProcessService.createProcessInstance(authToken, Constant.CONTAINER_ID, Constant.PROCESS_ID, Constant.CLIENT, "");

					if (StringUtil.isNotNullNotEmpty(jsonPayload.getProcessInstanceId())) {
						String taskId = jbpmProcessService.getOnlyTaskId(authToken, jsonPayload.getProcessInstanceId());

						if (StringUtil.isNotNullNotEmpty(taskId)) {
							lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken,
									Constant.CONTAINER_ID, taskId, Constant.CLIENT, jsonPayload.getProcessInstanceId(),
									jsonPayload);
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
									.body(lMappedVariableInstanceLogVO);
						}
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ed);
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	
	@PatchMapping("incidentmanagement/{incidentId}")
	public ResponseEntity<Object> patchIncident(@RequestHeader("Authorization") String authToken, @PathVariable("incidentId") String incidentId, @RequestBody HashMap<String, Object> jsonPayload)
	{
		System.out.println("inside patch");
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Page_incidentcreation)) {
				
				MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = incidentCreationServiceImpl.patchCheckAndUpdate(incidentId,jsonPayload);
				
				if(lMappedVariableInstanceLogVO!=null)
				{
					if(StringUtil.isNotNullNotEmpty(lMappedVariableInstanceLogVO.getProcessInstanceId()))
					{
						String taskId = jbpmProcessService.getOnlyTaskId(authToken, lMappedVariableInstanceLogVO.getProcessInstanceId());
						if(StringUtil.isNotNullNotEmpty(taskId))
						{
							lMappedVariableInstanceLogVO = incidentCreationService.completeTask(authToken, Constant.CONTAINER_ID,taskId, Constant.CLIENT, lMappedVariableInstanceLogVO.getProcessInstanceId(), lMappedVariableInstanceLogVO);
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(lMappedVariableInstanceLogVO);
						}
					}
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails.getUnAuthorized());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in completeTask: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError());
	}
	
	@PostMapping("/fetchallincidentistbasedongroup")
	public ResponseEntity<Object> fetchallIncidentList(@RequestHeader("Authorization") String authToken,
			@RequestBody String jsonPayLoadMap) {
		List<IncidentStatusEntity> lIncidentStatusEntity = new ArrayList<IncidentStatusEntity>();
		HashMap<String, String> filter = new HashMap<String, String>();

		try {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoadMap)) {
				filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
				if (filter != null && filter.size() > 0) {
					if (StringUtil.isNotNullNotEmpty(filter.get("assignmentGroup"))
							&& filter.containsKey("assignmentGroup")) {
						lIncidentStatusEntity = parentChildServiceImpl.getIncidentListForParentChild(jsonPayLoadMap);
						if (lIncidentStatusEntity != null) {
							return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
									.body(lIncidentStatusEntity);
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
