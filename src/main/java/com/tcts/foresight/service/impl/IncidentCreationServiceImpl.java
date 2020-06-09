package com.tcts.foresight.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.client.JBPMClientConfig;
import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.IncidentAttachmentEntity;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.IncidentHistoryUpdatedValues;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.ProgressBasedNotificationEntity;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.exception.CustomValidationException;
import com.tcts.foresight.jbpm.db.JBPMRowMapper;
import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;
import com.tcts.foresight.repository.CSATFeedbackRepo;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.IncidentAttachmentRepo;
import com.tcts.foresight.repository.IncidentHistoryRepo;
import com.tcts.foresight.repository.IncidentProgressNotificationRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.repository.UserDetailsSummary;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.scheduler.service.impl.SLAHistoryServiceImpl;
import com.tcts.foresight.service.IncidentCreationService;
import com.tcts.foresight.service.UserManagementService;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.DateUtil;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.ObjectDiffUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class IncidentCreationServiceImpl implements IncidentCreationService {
	Logger logger = LoggerFactory.getLogger(IncidentCreationServiceImpl.class);

	@Value("${RHPAM_DB_URL}")
	private String foresightURL;

	@Value("${RHPAM_DB_USER}")
	private String foresightUser;
	// @Autowired
	// SLAConfigurationRepo slaConfigRepo1;

	@Value("${RHPAM_DB_PASSWORD}")
	private String foresightPassword;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JBPMClientConfig jbpmClient;
	
	@Autowired
	private ParentChildServiceImpl parentChildServiceimpl;

	@Autowired
	private SLAHistoryServiceImpl lSLAHistoryServiceImpl;

	@Autowired
	private JbpmProcessServiceImpl jbpmProcessServiceImpl;

	@Autowired
	@Qualifier("auxDataSource")
	private DataSource auxDataSource;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;

	@Autowired
	private CategoryDetailsRepo catDetailsRepo;

	@Autowired
	private UserManagementService userMngService;

	@Autowired
	private ObjectDiffUtil lObjectDiffUtil;

	@Autowired
	private IncidentHistoryRepo lIncidentHistoryRepo;

	@Autowired
	private IncidentAttachmentRepo incidentAttachRepo;

	@Autowired
	private CSATFeedbackRepo lCSATRepo;
	
	@Autowired
	private ProblemNotificationImpl lProblemNotificationImpl;
	

	@Autowired
	private IncidentProgressNotificationRepo incidentProgressNotificationRepo;
	
	@Autowired
	ConfigEntityCached lConfigEntityCached;
	
	@Autowired
	IncidentNotificationImpl lIncidentNotificationImpl;

	@Override
	public MappedVariableInstanceLogVO completeTask(String authToken, String containerId, String taskInstanceId,
			String clientCode, String processInstanceId, MappedVariableInstanceLogVO mappedVariableInstaceLogVO)
			throws CustomValidationException {

		// logger.info("mappedVariableInstaceLogVO.getLastUpdatedBy(): " +
		// mappedVariableInstaceLogVO.getLastUpdatedBy());
		MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;
		MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLD = null;
		MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLDClone = null;
		MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW = null;
		Response response = null;
		JSONObject jsonResponse = null;
		boolean isFetchVariablesNeeded = true;
		String jsonData = null;
		String statusValue = null;
		String mappedVarInstacntLogVoString = null;
		//String defaultGroup = "YES";
		List<IncidentAttachmentEntity> incidentAttachmentList = mappedVariableInstaceLogVO.getIncidentAttachList();
		mappedVariableInstaceLogVO.setIncidentAttachList(null);
		
		// TODO ganesh get the old value from auxdb
		if (mappedVariableInstaceLogVO.getIncidentID() != null) {
			lMappedVariableInstanceLogVOOLD = mappedVariableInstanceRepo.findByticketID(mappedVariableInstaceLogVO.getIncidentID());

			if (lMappedVariableInstanceLogVOOLD != null && lMappedVariableInstanceLogVOOLD.getIncidentID() != null) {
				try {
					lMappedVariableInstanceLogVOOLDClone = (MappedVariableInstanceLogVO) lMappedVariableInstanceLogVOOLD.clone();
					logger.info("====================	lMappedVariableInstanceLogVOOLDClone===================="
							+ objMapper.writeValueAsString(lMappedVariableInstanceLogVOOLDClone));
				} catch (Exception e) {
					logger.error("Exception occured lMappedVariableInstanceLogVOOLD: " + e.getMessage(), e);

				}
			}
		}
		try {

			logger.info("authToken : " + authToken + " : containerId : " + containerId + " : taskInstanceId :"
					+ taskInstanceId + " : clientCode : " + clientCode + " : processInstanceId :  "
					+ processInstanceId);

			if (("".equalsIgnoreCase(mappedVariableInstaceLogVO.getAssignmentGroup())
					|| mappedVariableInstaceLogVO.getAssignmentGroup() == null)
					&& ("".equalsIgnoreCase(mappedVariableInstaceLogVO.getAssignTo())
							|| mappedVariableInstaceLogVO.getAssignTo() == null)) {
				// logger.info("Group and AssignTo Name Are as Null ");
				GroupSummary defaultGrpDetails = groupDtlsRepo.findDistinctByDefaultModuleGroupContaining(mappedVariableInstaceLogVO.getModule().toUpperCase());
				if (defaultGrpDetails != null) {
					// logger.info("Default Group Name :- " + defaultGrpDetails.getGroupName());
					mappedVariableInstaceLogVO.setAssignmentGroup(defaultGrpDetails.getGroupName());
				} else {
					throw new CustomValidationException(
							"Default Group is not found in database, please select the group in the form and submit");
					// return "Default Group is not found in database, please select the group inthe
					// form and submit";
				}
			}

			// If Incident Status is Resolved, it will fetch autoClose time to set the value
			// to close TT Automatically
			if (mappedVariableInstaceLogVO.getStatus() != null
					&& mappedVariableInstaceLogVO.getStatus().equalsIgnoreCase("Resolved")) {
				// logger.info("===============Inside resolved=============");
				Long catId = catDetailsRepo.findIdByName(mappedVariableInstaceLogVO.getCategory(),
						mappedVariableInstaceLogVO.getModule());
				String resolvedTimer = userMngService.fetchAutoCloseTime(catId,
						mappedVariableInstaceLogVO.getPriority(), mappedVariableInstaceLogVO.getModule());
				// logger.info("Auto Close Time :- " + resolvedTimer);
				mappedVariableInstaceLogVO.setResolvedTimer(resolvedTimer);
			}

			setDefaultAttributeFromDB(lMappedVariableInstanceLogVOOLD, mappedVariableInstaceLogVO);

			mappedVarInstacntLogVoString = objMapper.writeValueAsString(mappedVariableInstaceLogVO);
			// logger.info("Create Ticket of Json Request Data:- " +
			// mappedVarInstacntLogVoString);
			JSONObject jsonPayloadRequest = new JSONObject(mappedVarInstacntLogVoString);

			// JBPM START TASK API
			String path = "containers/" + containerId + "/tasks/" + taskInstanceId + "/states/started";
			// logger.info("start path : " + path);
			jbpmClient.putRequest(authToken, path, "");
			// JBPM COMPLETE TASK API
			path = "containers/" + containerId + "/tasks/" + taskInstanceId + "/states/completed";
			// logger.info("completed path : " + path);

			response = jbpmClient.putRequest(authToken, path, mappedVarInstacntLogVoString);

			if ((response != null && (response.getStatus() == 200 || response.getStatus() == 201))) {
				
				// if (jsonPayloadRequest != null && jsonPayloadRequest.has("status")) {
				// statusValue = jsonPayloadRequest.get("status").toString();
				// if (statusValue != null && statusValue.equalsIgnoreCase("Closed")) {
				// isFetchVariablesNeeded = false;
				// } else if (statusValue != null && statusValue.equalsIgnoreCase("Cancelled"))
				// {
				// isFetchVariablesNeeded = false;
				// }
				// }

				// //logger.info("Is Fetch Variables Needed:- " + isFetchVariablesNeeded);
				// if (isFetchVariablesNeeded) {
				// path = "containers/" + containerId + "/processes/instances/" +
				// processInstanceId + "/variables";
				// jsonData = jbpmClient.getRequest(authToken, path).readEntity(String.class);
				// if (jsonData != null) {
				// //logger.info("Fetched Varaibles of Json :- " + jsonData.toString());
				// jsonResponse = new JSONObject(jsonData);
				// jsonResponse.put("processInstanceID", processInstanceId);
				// mappedVariableInstaceLogVO.setIncidentID(jsonResponse.get("incidentID").toString());
				// jsonData = jsonResponse.toString();
				// }
				// }

				lMappedVariableInstanceLogVO = checkAndInsertInAuxDBVaibhav(processInstanceId);
				logger.info("lMappedVariableInstanceLogVONEW============================ - "+ lMappedVariableInstanceLogVO.getIncidentID());

				// Inserting the Attachment List in AUXDB By Madhav
				mappedVariableInstaceLogVO.setIncidentAttachList(incidentAttachmentList);
				if (mappedVariableInstaceLogVO.getIncidentAttachList() != null && mappedVariableInstaceLogVO.getIncidentAttachList().size() > 0) {
					List<IncidentAttachmentEntity> incidentAttachList = checkAndInsertAttachmentInAuxDB(lMappedVariableInstanceLogVO.getIncidentID(),mappedVariableInstaceLogVO.getIncidentAttachList());
					lMappedVariableInstanceLogVO.setIncidentAttachList(incidentAttachList);
				}

				// TODO ganesh get the new value from auxdb
				if (lMappedVariableInstanceLogVO.getIncidentID() != null) {
					lMappedVariableInstanceLogVONEW = mappedVariableInstanceRepo
							.findByticketID(lMappedVariableInstanceLogVO.getIncidentID());
//					try {
//						logger.info("====================lMappedVariableInstanceLogVONEW===================="
//								+ objMapper.writeValueAsString(lMappedVariableInstanceLogVONEW));
//
//					} catch (JsonProcessingException e) {
//						logger.error("Exception occured lMappedVariableInstanceLogVONEW" + e.getMessage(), e);
//
//					}
					// TODO ganesh get the diff of old and new
					checkDiffreneceAndCreateHistory(lMappedVariableInstanceLogVOOLDClone,
							lMappedVariableInstanceLogVONEW);
					// TODO ganesh insert the diff in history table

					lSLAHistoryServiceImpl.checkSLAHistory(lMappedVariableInstanceLogVONEW);

					setStatusDEtails(lMappedVariableInstanceLogVONEW);

					// Updating childs

					parentChildServiceimpl.fetchAndUpdateAllChilds(authToken, containerId, taskInstanceId, clientCode,
							processInstanceId, mappedVariableInstaceLogVO);

					
					
					lIncidentNotificationImpl.checkAndSendNotifications(lMappedVariableInstanceLogVOOLDClone, lMappedVariableInstanceLogVONEW);
						
					
					
					
					// dhiraj
					//sent status based notification

					ProgressBasedNotificationEntity progressBasedNotificationEntity = incidentProgressNotificationRepo.findFirstByOrderByIdAsc();
					
					if (mappedVariableInstaceLogVO != null 
							|| !progressBasedNotificationEntity.getEmailList().equalsIgnoreCase("")
							|| !progressBasedNotificationEntity.getPhoneNumberList().equalsIgnoreCase("")) {
						

						boolean flag = false;
						if (lMappedVariableInstanceLogVOOLD == null) {
							flag = true;
							if (lMappedVariableInstanceLogVO.getIncidentID() != null) {
								lIncidentNotificationImpl.statusChangedNotification(lMappedVariableInstanceLogVONEW, flag);
							}
						} else {
							if (!lMappedVariableInstanceLogVOOLDClone.getStatus().equalsIgnoreCase(lMappedVariableInstanceLogVONEW.getStatus())) {
								lIncidentNotificationImpl.statusChangedNotification(lMappedVariableInstanceLogVONEW, flag);
							}
						}
					}
					
					if(lMappedVariableInstanceLogVOOLD==null){
						lProblemNotificationImpl.checkAndCreateProblemNotification(lMappedVariableInstanceLogVONEW);
					}
						
				}
			} else {
				// logger.info("TASK IS NOT COMPLETED");
				throw new CustomValidationException("TASK IS NOT COMPLETED");
			}
			
			

			
		} catch (CustomValidationException custValidEx) {
			logger.error("CustomValidationException---" + custValidEx.getMessage(), custValidEx);
			throw custValidEx;
		} catch (Exception e) {
			logger.error("Exception occured in completeTask " + e.getMessage(), e);
			throw new CustomValidationException("Exception occured in completeTask " + e.getMessage());
		}
		return lMappedVariableInstanceLogVO;
	}


	private void setDefaultAttributeFromDB(MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLD,
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVONew) {
		// TODO Auto-generated method stub
		if (lMappedVariableInstanceLogVOOLD != null) {
			lMappedVariableInstanceLogVONew
					.setResolutionSlaBreach(lMappedVariableInstanceLogVOOLD.getResolutionSlaBreach());
			lMappedVariableInstanceLogVONew
					.setResponseSlaBreach(lMappedVariableInstanceLogVOOLD.getResponseSlaBreach());
			System.out.println(
					"The ResolutionSlABracket Value is:" + lMappedVariableInstanceLogVOOLD.getResolutionSlaBracket());
			lMappedVariableInstanceLogVONew
					.setResolutionSlaBracket(lMappedVariableInstanceLogVOOLD.getResolutionSlaBracket());
		}

	}

	private void setStatusDEtails(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {

	}

	private void checkDiffreneceAndCreateHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLD,
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		if (lMappedVariableInstanceLogVOOLD == null) {
			long initZeroTimeTaken = 0l;
			IncidentHistoryEntity lIncidentHistoryEntity = new IncidentHistoryEntity();
			lIncidentHistoryEntity.setUpdatedBy(lMappedVariableInstanceLogVONEW.getCreatedByFullName());
			lIncidentHistoryEntity.setUpdatedDate(lMappedVariableInstanceLogVONEW.getIncidentCreationDate());
			lIncidentHistoryEntity.setComments(lMappedVariableInstanceLogVONEW.getComments());
			lIncidentHistoryEntity.setIncidentID(lMappedVariableInstanceLogVONEW.getIncidentID());
			lIncidentHistoryEntity.setMilestone("Incident Created");// by
			lIncidentHistoryEntity.setTimeTaken(initZeroTimeTaken);
			// "+lMappedVariableInstanceLogVONEW.getCreatedByFullName());
			lIncidentHistoryRepo.save(lIncidentHistoryEntity);
		} else {
			List<IncidentHistoryEntity> lIncidentHistoryEntityList = lObjectDiffUtil
					.checkDifference(lMappedVariableInstanceLogVOOLD, lMappedVariableInstanceLogVONEW);
			if (lIncidentHistoryEntityList != null && lIncidentHistoryEntityList.size() > 0) {
				for (IncidentHistoryEntity lIncidentHistoryEntity : lIncidentHistoryEntityList) {
					if (lIncidentHistoryEntity != null) {
						lIncidentHistoryEntity.setUpdatedBy(lMappedVariableInstanceLogVONEW.getLastUpdatedBy());
						lIncidentHistoryEntity.setUpdatedDate(lMappedVariableInstanceLogVONEW.getLastUpdatedDate());
						lIncidentHistoryEntity.setComments(lMappedVariableInstanceLogVONEW.getComments());
						lIncidentHistoryEntity.setIncidentID(lMappedVariableInstanceLogVONEW.getIncidentID());
						lIncidentHistoryEntity.setMilestone("Incident Updated");// by
						// "+lMappedVariableInstanceLogVONEW.getLastUpdatedBy());
						// Setting Time Taken in MilliSeconds
						Calendar Start = DateUtil.stringToCal(lMappedVariableInstanceLogVOOLD.getIncidentCreationDate(),
								sdf);
						if (StringUtil.isNotNullNotEmpty(lMappedVariableInstanceLogVOOLD.getLastUpdatedDate())) {
							Start = DateUtil.stringToCal(lMappedVariableInstanceLogVOOLD.getLastUpdatedDate(), sdf);
						}
						Calendar End = DateUtil.stringToCal(lMappedVariableInstanceLogVONEW.getLastUpdatedDate(), sdf);
						lIncidentHistoryEntity.setTimeTaken(End.getTimeInMillis() - Start.getTimeInMillis());
					}
				}

				lIncidentHistoryRepo.saveAll(lIncidentHistoryEntityList);
			}

		}

	}

	private void checkAndInsertInAuxDB(String processInstanceId) {
		JSONObject newObject = new JSONObject();
		newObject.put("process-instance-id", processInstanceId);
		List<MappedVariableInstanceLogVO> mappedVariableInstanceLogVOList = fetchData(newObject.toString());
		for (MappedVariableInstanceLogVO mappedVariableInstanceLogVO : mappedVariableInstanceLogVOList) {

			// logger.info("Assign To Fetch Full Name:- " +
			// mappedVariableInstanceLogVO.getAssignTo());
			if (!"".equalsIgnoreCase(mappedVariableInstanceLogVO.getAssignTo())) {
				mappedVariableInstanceLogVO
						.setFullName(userRepo.findFullNameByEmail(mappedVariableInstanceLogVO.getAssignTo()));
			}
			// logger.info("Full Name:- " + mappedVariableInstanceLogVO.getFullName());
			mappedVariableInstanceRepo.save(mappedVariableInstanceLogVO);
		}
	}

	public MappedVariableInstanceLogVO checkAndInsertInAuxDBVaibhav(String processInstanceId) {
		JSONObject newObject = new JSONObject();
		newObject.put("process-instance-id", processInstanceId);
		MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = null;

		List<MappedVariableInstanceLogVO> mappedVariableInstanceLogVOList = fetchData(newObject.toString());
		for (MappedVariableInstanceLogVO mappedVariableInstanceLogVO : mappedVariableInstanceLogVOList) {

			// logger.info("Assign To Fetch Full Name:- " +
			// mappedVariableInstanceLogVO.getAssignTo());
			if (!"".equalsIgnoreCase(mappedVariableInstanceLogVO.getAssignTo())) {
				mappedVariableInstanceLogVO
						.setFullName(userRepo.findFullNameByEmail(mappedVariableInstanceLogVO.getAssignTo()));
			}
			logger.info("mappedVariableInstanceLogVO JBPM mappedVariableInstanceLogVO.getStatus():- "
					+ mappedVariableInstanceLogVO.getStatus());
			lMappedVariableInstanceLogVO = mappedVariableInstanceLogVO;
			lMappedVariableInstanceLogVO = mappedVariableInstanceRepo.save(mappedVariableInstanceLogVO);

		}
		return lMappedVariableInstanceLogVO;
	}

	@Override
	public List<MappedVariableInstanceLogVO> filteredJobList(String jsonPayLoadMap) {
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		mappedList = fetchDataFromAuxDB(jsonPayLoadMap);
		return mappedList;
	}

	@Override
	public List<MappedVariableInstanceLogVO> filteredJobList1(String userName) {
		Set<GroupSummary> groupDtls = groupDtlsRepo.findGroupListByUser_Email(userName);
		String groupListAsString = groupDtls.stream().filter(group -> group.getGroupName() != null)
				.map(GroupSummary::getGroupName).collect(Collectors.joining("','", "'", "'"));

		// logger.info("Group List In String:- " + groupListAsString);

		String query = "select * from mapped_variable_instance_log where string_to_array(group_name, ',') && array["
				+ groupListAsString + "]";

		return findByQuery(query);
	}

	private List<MappedVariableInstanceLogVO> fetchDataFromAuxDB(String jsonPayLoadMap) {

		HashMap<String, String> queryMap = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> queryMapList = new HashMap<String, ArrayList<String>>();
		String query = "from MappedVariableInstanceLogVO where ";
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		HashMap<String, String> filter = new HashMap<String, String>();
		String groupListAsString = null;

		if (jsonPayLoadMap != null && !jsonPayLoadMap.trim().equals("")) {
			filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);

			if (filter != null && filter.size() > 0) {
				for (String key : filter.keySet()) {
					
					
					
					String value = filter.get(key);
					
					// check and return for sql Injection
                    if(value.toLowerCase().contains(" or ") 
                                  || value.toLowerCase().contains(" and ")
                                  || value.toLowerCase().contains("'or ")
                                  || value.toLowerCase().contains("'and ")
                                  
                                  )
                    {
                           return null;
                    }	
					
					if (key.equalsIgnoreCase("start-date") || key.equalsIgnoreCase("end-date")) {
						continue;
					}

					// NOT CONDITION
					if (filter.get(key) != null && filter.get(key).contains("!")) {
						ArrayList<String> andmultipleValues = andMultipleConditionValuesPresentList(filter.get(key));

						if (andmultipleValues != null && andmultipleValues.size() > 0) {
							query = query + key + " NOT IN (:" + key + ") AND ";
							queryMapList.put(key, andmultipleValues);

						}

					} else {
						ArrayList<String> multipleValues = multipleValuesPresentList(filter.get(key));

						if (multipleValues != null) {
							query = query + key + " IN (:" + key + ") AND ";
							queryMapList.put(key, multipleValues);

						} else if (key.equalsIgnoreCase("userName")) {
							Set<GroupSummary> groupDtls = groupDtlsRepo
									.findGroupListByUser_Email(filter.get("userName"));
							groupListAsString = groupDtls.stream().filter(group -> group.getGroupName() != null)
									.map(GroupSummary::getGroupName).collect(Collectors.joining("','", "'", "'"));
							query = query + "string_to_array(group_name, ',') && array[" + groupListAsString + "] AND ";
						} else if (filter.get(key) == "null") {
							query = query + key + " is null AND ";
						} else {
							query = query + key + "= :" + key + " AND ";
							queryMap.put(key, filter.get(key));
						}
					}

				}

				if (filter.containsKey("start-date") || filter.containsKey("end-date")) {
					query = query + "created_date BETWEEN :created_start_date AND :created_end_date";
					queryMap.put("created_start_date", filter.get("start-date") + " 00:00:00");
					queryMap.put("created_end_date", filter.get("end-date") + " 23:59:59");
				}

				if (query.endsWith("AND ")) {
					query = query.substring(0, query.length() - 4);
				}

				mappedList = findByQueryFiltered(query, queryMap, queryMapList);
			}
		}
		return mappedList;
	}

	private List<MappedVariableInstanceLogVO> fetchData(String jsonPayLoadMap) {
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		HashMap<String, String> filter = new HashMap<String, String>();
		filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
		boolean dateRangeFilter = false;

		String finalQery = "select * from variableinstancelog where processinstanceid in (INNER_QUERY)";
		String populatedQueryOriginal = "select processinstanceid from variableinstancelog";

		String internalSubQuery = populatedQueryOriginal;

		if (filter.containsKey("start-date") || filter.containsKey("end-date")) {
			dateRangeFilter = true;
		}
		String whereClause = " where ";
		if (filter != null && filter.size() > 0) {
			int size = 0;
			for (String key : filter.keySet()) {
				if (key.equalsIgnoreCase("start-date") || key.equalsIgnoreCase("end-date")) {
					continue;
				}
				if (size == 0 && key.equalsIgnoreCase("process-instance-id")) {
					internalSubQuery = populatedQueryOriginal + whereClause + "processinstanceid = '" + filter.get(key)
							+ "'";
				}
				if (size == 0 && !(key.equalsIgnoreCase("process-instance-id"))) {

					String multipleValue = multipleValuesPresent(filter.get(key));

					if (multipleValue != null) {
						whereClause = whereClause + "variableid = '" + key + "' and value in (" + multipleValue + ")";
					} else {
						whereClause = whereClause + "variableid = '" + key + "' and value = '" + filter.get(key) + "'";
					}
					internalSubQuery = internalSubQuery + whereClause;
				} else if (size < filter.size() && !(key.equalsIgnoreCase("process-instance-id"))) {
					String internalSubQuery1 = populatedQueryOriginal;
					String whereClause1 = " where ";

					String multipleValue = multipleValuesPresent(filter.get(key));

					if (multipleValue != null) {
						whereClause1 = whereClause1 + "variableid = '" + key + "' and value in (" + multipleValue + ")";
					} else {
						whereClause1 = whereClause1 + "variableid = '" + key + "' and value = '" + filter.get(key)
								+ "'";
					}
					internalSubQuery1 = internalSubQuery1 + whereClause1;
					internalSubQuery = internalSubQuery + " and processinstanceid in (" + internalSubQuery1 + ")";
				}
				size = size + 1;
			}
		}

		if (filter != null && filter.size() > 0 && filter.containsKey("start-date") && filter.containsKey("end-date")) {

			String dateRangeWhereClause = " where variableid = 'createdDateTimeZone' AND VALUE BETWEEN '"
					+ filter.get("start-date") + " 00:00:00' AND '" + filter.get("end-date") + " 23:59:59'";
			String dateRangeQuery = populatedQueryOriginal + dateRangeWhereClause;

			if (internalSubQuery.contains("where")) {
				internalSubQuery = internalSubQuery + " and processinstanceid in (" + dateRangeQuery + ")";
			} else {
				internalSubQuery = internalSubQuery + " where processinstanceid in (" + dateRangeQuery + ")";
			}
		}

		finalQery = finalQery.replace("INNER_QUERY", internalSubQuery);
		finalQery = finalQery + " order by log_date desc";

		ArrayList<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO = new ArrayList<ActualVariableInstanceLogVO>();
		Connection connection = null;
		Statement selectStmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.postgresql.Driver");
			// connection = DriverManager.getConnection(RHPAM_DB_URL, RHPAM_DB_USER,
			// RHPAM_DB_PASSWORD);
			//// //logger.info(": skyloURL :"+skyloURL+ " : skyloUser : "+skyloUser+ ":
			// skyloPassword : "+skyloPassword );
			if (!foresightURL.equals("") && !foresightUser.equals("") && !foresightPassword.equals("")) {
				connection = DriverManager.getConnection(foresightURL, foresightUser, foresightPassword);
			}
			// logger.info("finalQery - " + finalQery);

			selectStmt = connection.createStatement();
			rs = selectStmt.executeQuery(finalQery);
			while (rs.next()) {
				lActualVariableInstanceLogVO.add(new ActualVariableInstanceLogVO(rs.getString("processInstanceId"),
						rs.getString("processId"), rs.getString("value"), rs.getString("variableid")));

			}
			// logger.info("lActualVariableInstanceLogVO - " +
			// lActualVariableInstanceLogVO);
			JBPMRowMapper rowmapper = new JBPMRowMapper();
			mappedList = rowmapper.getConvertedJBPMResponseAsList(lActualVariableInstanceLogVO);
			for (String key : filter.keySet()) {
				Iterator<MappedVariableInstanceLogVO> itr = mappedList.iterator();
				// remove all even numbers
				while (itr.hasNext()) {
					MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = itr.next();

					Method methods[] = lMappedVariableInstanceLogVO.getClass().getMethods();
					for (Method method : methods) {
						String methodName = method.getName();
						if (methodName.startsWith("get")) {
							String lowerMEthodName = methodName.toLowerCase();
							String subStringMethodName = lowerMEthodName.substring(3, lowerMEthodName.length());
							if (subStringMethodName.equalsIgnoreCase(key)) {
								Method callingClassMEthod = lMappedVariableInstanceLogVO.getClass()
										.getMethod(methodName);
								String response = (String) callingClassMEthod.invoke(lMappedVariableInstanceLogVO);
								if (response != null && response.equals((String) filter.get(key))) {
								} else {
									if (filter.containsKey("start-date") || filter.containsKey("end-date")) {

									} else {
										itr.remove();
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("Exception occur while in fetchData " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (selectStmt != null) {
					selectStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				logger.error("Exception occur while in fetchData.... finally  " + e.getMessage(), e);
			}
		}
		return mappedList;
	}

	private String multipleValuesPresent(String stringValue) {

		String returnString = null;
		if (stringValue != null && stringValue.contains(",")) {

			StringBuilder finalString = new StringBuilder();

			StringTokenizer token = new StringTokenizer(stringValue, ",");

			while (token.hasMoreTokens()) {
				String tokenValue = token.nextToken().trim();

				finalString.append("'" + tokenValue + "',");
			}

			returnString = finalString.substring(0, finalString.length() - 1);

		}

		return returnString;
	}

	private ArrayList<String> multipleValuesPresentList(String stringValue) {

		ArrayList<String> list = null;
		if (stringValue != null && stringValue.contains(",")) {
			StringBuilder finalString = new StringBuilder();
			StringTokenizer token = new StringTokenizer(stringValue, ",");
			list = new ArrayList<String>();
			while (token.hasMoreTokens()) {
				String tokenValue = token.nextToken().trim();
				list.add(tokenValue);
				finalString.append("'" + tokenValue + "',");
			}
		}
		return list;
	}

	private ArrayList<String> andMultipleConditionValuesPresentList(String stringValue) {

		ArrayList<String> list = null;
		if (stringValue != null && stringValue.contains("!")) {
			StringBuilder finalString = new StringBuilder();
			StringTokenizer token = new StringTokenizer(stringValue, ",");
			list = new ArrayList<String>();
			while (token.hasMoreTokens()) {
				String tokenValue = token.nextToken().trim();
				if (tokenValue.contains("!")) {
					tokenValue = tokenValue.replaceAll("!", "");
					tokenValue = tokenValue.trim();
					list.add(tokenValue);
					finalString.append("'" + tokenValue + "',");
				}

			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<MappedVariableInstanceLogVO> findByQuery(String query) {
		List<MappedVariableInstanceLogVO> mappedVariable = entityManager.createNativeQuery(query, MappedVariableInstanceLogVO.class).getResultList();
		return mappedVariable;
	}

	@SuppressWarnings("unchecked")
	public List<MappedVariableInstanceLogVO> findByQueryFiltered(String query, HashMap<String, String> queryMap,
			HashMap<String, ArrayList<String>> queryMapList) {
		
		Query mappedVariableQuery = entityManager.createQuery(query, MappedVariableInstanceLogVO.class);
		if (queryMap != null && queryMap.size() > 0) {
			for (String key : queryMap.keySet()) {
				mappedVariableQuery.setParameter(key, queryMap.get(key));
			}
		}

		if (queryMapList != null && queryMapList.size() > 0) {
			for (String key : queryMapList.keySet()) {
				mappedVariableQuery.setParameter(key, queryMapList.get(key));
			}
		}

		List<MappedVariableInstanceLogVO> mappedVariable = mappedVariableQuery.getResultList();
		return mappedVariable;
	}

	@Override
	public HashMap<String, String> assignToMe(String authToken, String containerId,
			List<MappedVariableInstanceLogVO> jsonPayload, String clientCode) {
		List<MappedVariableInstanceLogVO> jsonList = jsonPayload;
		HashMap<String, String> responseMap = new HashMap<String, String>();
		// logger.info("List<MappedVariableInstanceLogVO> jsonList" + jsonList);
		try {
			for (MappedVariableInstanceLogVO mappedList : jsonList) {
				try {
					String jsonData = null;
					String taskID = null;
					String processInstanceId = mappedList.getProcessInstanceId();
					// logger.info("MMMMMMMMMMMMMMMMMM" + mappedList.getProcessInstanceId());
					taskID = jbpmProcessServiceImpl.getTaskId(authToken, mappedList.getProcessInstanceId());
					// getting Task ID
					JSONObject jobj = new JSONObject(taskID);
					JSONArray jArray = jobj.getJSONArray("task-summary");
					String taskInstanceId = jArray.getJSONObject(0).optString("task-id");
					// logger.info("Hey this is your task-id-------" + taskInstanceId);
					// logger.info("mappedList---------" + mappedList);

					mappedList.setComments("Self-assigned by user");
					MappedVariableInstanceLogVO mappedVarInstacntLogVo = completeTask(authToken, containerId,
							taskInstanceId, clientCode, processInstanceId, mappedList);
					if (responseMap.containsKey("successIncidents")) {
						String currentValue = responseMap.get("successIncidents");
						currentValue = currentValue + ", " + mappedList.getIncidentID();
						responseMap.put("successIncidents", currentValue);
					} else {
						responseMap.put("successIncidents", mappedList.getIncidentID());
					}
				} catch (CustomValidationException e) {
					logger.error("-----Exception occurred in assignToMe-------" + e.getMessage(), e);
					if (responseMap.containsKey("failedIncidents")) {
						String currentValue = responseMap.get("failedIncidents");
						currentValue = currentValue + ", " + mappedList.getIncidentID();
						responseMap.put("failedIncidents", currentValue);
					} else {
						responseMap.put("failedIncidents", mappedList.getIncidentID());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occur while in assignToMe " + e.getMessage(), e);
			return null;
		}
		return responseMap;
	}

	@Override
	public List<MappedVariableInstanceLogVO> filteredJobListByPagination(Integer pageNo, Integer pageSize,
			String sortBy, String jsonPayLoadMap) {
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		mappedList = fetchDataFromAuxDB(jsonPayLoadMap);

		org.springframework.data.domain.Pageable paging = PageRequest.of(pageNo, pageSize,
				org.springframework.data.domain.Sort.by(sortBy));

		Page<MappedVariableInstanceLogVO> pagedResult = mappedVariableInstanceRepo.findAll(paging);

		if (pagedResult.hasContent()) {
			// logger.info("pagedResult.getContent().size(): " +
			// pagedResult.getContent().size());

			return pagedResult.getContent();
		} else {
			return new ArrayList<MappedVariableInstanceLogVO>();
		}
	}

	@Override
	public List<MappedVariableInstanceLogVO> fetchAllIncidentList(String incidentID) {
		try {
			MappedVariableInstanceLogVO mappedVariableInstanceLogVO = mappedVariableInstanceRepo
					.findByIncidentID(incidentID);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured in fetchAllIncidentList" + e.getMessage(), e);

		}
		return mappedVariableInstanceRepo.findAll().stream()
				.filter(name -> name.getIncidentID().equalsIgnoreCase(incidentID)).collect(Collectors.toList());
	}

	@Override
	public List<IncidentHistoryEntity> fetchAllIncidentHistory(String incidentID) {

		List<IncidentHistoryEntity> finalHistory = new ArrayList<IncidentHistoryEntity>();

		List<IncidentHistoryEntity> list = lIncidentHistoryRepo.findByticketID(incidentID);

		HashMap<String, List<IncidentHistoryEntity>> map = new HashMap<String, List<IncidentHistoryEntity>>();

		if (list != null && list.size() > 0)

		{

			for (IncidentHistoryEntity history : list)

			{

				if (map.containsKey(history.getUpdatedDate()))

				{

					List<IncidentHistoryEntity> filteredList = map.get(history.getUpdatedDate());

					filteredList.add(history);

					map.put(history.getUpdatedDate(), filteredList);

				} else {
					List<IncidentHistoryEntity> filteredList = new ArrayList<IncidentHistoryEntity>();

					filteredList.add(history);

					map.put(history.getUpdatedDate(), filteredList);

				}

			}

			for (String key : map.keySet())

			{

				IncidentHistoryEntity incidentHistoryEntity = null;

				List<IncidentHistoryEntity> value = map.get(key);

				incidentHistoryEntity = value.get(0);

				List<IncidentHistoryUpdatedValues> incidentHistoryUpdatedValuesList = new ArrayList<IncidentHistoryUpdatedValues>();

				for (IncidentHistoryEntity history : value)

				{

					IncidentHistoryUpdatedValues incidentHistoryUpdatedValues = new IncidentHistoryUpdatedValues();

					incidentHistoryUpdatedValues.setFieldName(history.getFieldName());

					incidentHistoryUpdatedValues.setOldValue(history.getOldValue());

					incidentHistoryUpdatedValues.setNewValue(history.getNewValue());

					incidentHistoryUpdatedValuesList.add(incidentHistoryUpdatedValues);

				}

				incidentHistoryEntity.setIncidentHistoryUpdatedValuesList(incidentHistoryUpdatedValuesList);

				finalHistory.add(incidentHistoryEntity);

			}

		}

		Collections.sort(finalHistory);

		return finalHistory;

	}

	@Override
	public HashMap<String, String> resolveIncidentTT(String authToken, String containerId, String clientCode,
			List<MappedVariableInstanceLogVO> jsonPayload) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		List<MappedVariableInstanceLogVO> returnJsonList = new ArrayList<MappedVariableInstanceLogVO>();
		// logger.info("List<MappedVariableInstanceLogVO> jsonList" + jsonPayload);
		try {
			for (MappedVariableInstanceLogVO mappedList : jsonPayload) {
				try {
					String taskID = null;
					String processInstanceId = mappedList.getProcessInstanceId();
					// logger.info("Process Instance Id:- " + mappedList.getProcessInstanceId());
					taskID = jbpmProcessServiceImpl.getTaskId(authToken, mappedList.getProcessInstanceId());
					// logger.info("taskID - " + taskID);
					// getting Task ID
					JSONObject jobj = new JSONObject(taskID);
					JSONArray jArray = jobj.getJSONArray("task-summary");
					if (jArray != null && jArray.length() > 0 && jArray.getJSONObject(0) != null) {
						String taskInstanceId = jArray.getJSONObject(0).optString("task-id");
						// logger.info("Bulk Resolve Incident TT of Task Id------" + taskInstanceId);
						// logger.info("Mapped List --------> " + mappedList);

						try {
							mappedList.setStatus(Constant.INCIDENT_STATUS_RESOLVED);
							// logger.info("Incident TT Status Re-Checking:- " + mappedList.getStatus());
							MappedVariableInstanceLogVO mappedVarInstacntLogVo = completeTask(authToken, containerId,
									taskInstanceId, clientCode, processInstanceId, mappedList);
							if (responseMap.containsKey("successIncidents")) {
								String currentValue = responseMap.get("successIncidents");
								currentValue = currentValue + ", " + mappedList.getIncidentID();
								responseMap.put("successIncidents", currentValue);
							} else {
								responseMap.put("successIncidents", mappedList.getIncidentID());
							}
							returnJsonList.add(mappedVarInstacntLogVo);

						} catch (Exception e) {
							logger.error("Exception Ocurred in resolveIncidentTT: " + e.getMessage(), e);
							if (responseMap.containsKey("failedIncidents")) {
								String currentValue = responseMap.get("failedIncidents");
								currentValue = currentValue + ", " + mappedList.getIncidentID();
								responseMap.put("failedIncidents", currentValue);
							} else {
								responseMap.put("failedIncidents", mappedList.getIncidentID());
							}

						}

					} else {
						if (responseMap.containsKey("failedIncidents")) {
							String currentValue = responseMap.get("failedIncidents");
							currentValue = currentValue + ", " + mappedList.getIncidentID();
							responseMap.put("failedIncidents", currentValue);
						} else {
							responseMap.put("failedIncidents", mappedList.getIncidentID());
						}
					}

				} catch (Exception e) {
					logger.error("Exception occurred in resolveIncidentTT: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Exception occur while Resolving TT" + e.getMessage(), e);
			return null;
		}
		return responseMap;
	}

	public List<IncidentAttachmentEntity> checkAndInsertAttachmentInAuxDB(String incidentID,
			List<IncidentAttachmentEntity> incidentAttachmentList) {
		IncidentAttachmentEntity returnincdntAttE = null;
		List<IncidentAttachmentEntity> returnincdntAttList = new ArrayList<IncidentAttachmentEntity>();

		for (IncidentAttachmentEntity incdntAttE : incidentAttachmentList) {
			incdntAttE.setIncidentID(incidentID);
			returnincdntAttE = incidentAttachRepo.save(incdntAttE);
			returnincdntAttList.add(returnincdntAttE);
		}
		return returnincdntAttList;
	}

	@Override
	public List<IncidentAttachmentEntity> addAttachment(List<IncidentAttachmentEntity> incidentAttachmentList) {
		return incidentAttachRepo.saveAll(incidentAttachmentList);
	}

	// Delete Incident Attachment By Id
	@Override
	public void deleteAttachment(Long attachmentId, String authToken) {
		try {
			String incidentAttachmentName = incidentAttachRepo.getAttachmentName(attachmentId.longValue());
			String incidentID = incidentAttachRepo.getIncidentIdAttachment(attachmentId.longValue());
			
			
			if(StringUtil.isNotNullNotEmpty(incidentAttachmentName))
			{
				
				String userFullName = userMngService.getFullUserNameFromAuth(authToken);
				String attachmentName="";
				String incidentId="";
				attachmentName = incidentAttachmentName;
				incidentId = incidentID;
				incidentAttachRepo.deleteById(attachmentId);
				IncidentHistoryEntity lIncidentHistoryEntity = new IncidentHistoryEntity();
				lIncidentHistoryEntity.setUpdatedBy(userFullName);
				lIncidentHistoryEntity.setUpdatedDate(DateUtil.currentDateInString()); 
				lIncidentHistoryEntity.setComments("Attachment is deleted"); 
				lIncidentHistoryEntity.setIncidentID(incidentId);
				lIncidentHistoryEntity.setMilestone("Incident Updated");// by
		        lIncidentHistoryEntity.setFieldName("Attachment");
		        lIncidentHistoryEntity.setOldValue(attachmentName+" was present in DB");
		        lIncidentHistoryEntity.setNewValue(attachmentName+" is removed from DB");
		        lIncidentHistoryRepo.save(lIncidentHistoryEntity);
				
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	// Fetching All Incident Attachments
	@Override
	public List<IncidentAttachmentEntity> fetchIncidentAttchmentList(String ticketId) {
		return incidentAttachRepo.findByIncidentID(ticketId);
	}

	// Fetching All visited status by id
	@Override
	public HashMap<String, List<String>> fetchStatusVisited(String ticketId) {
		List<String> statusList = new ArrayList<String>();
		HashMap<String, List<String>> statusVisited = new HashMap<String, List<String>>();
		MappedVariableInstanceLogVO ticketInfo = mappedVariableInstanceRepo.findByIncidentID(ticketId);
		List<IncidentHistoryEntity> incidentHistoryEntity = lIncidentHistoryRepo.findAllStatusById(ticketId);
		for (IncidentHistoryEntity inicidentList : incidentHistoryEntity) {
			statusList.add(inicidentList.getOldValue());
			statusList.add(inicidentList.getNewValue());
		}
		statusList.add(Constant.INCIDENT_STATUS_NEW); // By default Ticket Lifecycle starts with new
		List<String> finalList = statusList.stream().distinct().collect(Collectors.toList());
		finalList.remove(ticketInfo.getStatus());
		statusVisited.put("statusVisited", finalList);
		return statusVisited;
	}

	@Override
	public List<String> getIncidentId(List<String> status) {
		return mappedVariableInstanceRepo.findByStatus(status);
	}

	@Override
	public List<String> fechAllRecords(String module, String statusClosed, String statusCancelled) {
		return mappedVariableInstanceRepo.findBymoduleandStatus(module, statusClosed, statusCancelled);
	}

//	@Override
//	public List<MappedVariableInstanceLogVO> filteredjoblistWithGroupVisited(String jsonPayLoadMap) {
//		HashMap<String, String> filter = new HashMap<String, String>();
//		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
//		List<String> ticketList = new ArrayList<String>();
//		String groupVisited = null;
//		String payLoadNew = null;
//		try {
//
//			if (jsonPayLoadMap != null && !jsonPayLoadMap.trim().equals("")) {
//				filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
//
//				if (filter != null && filter.size() > 0) {
//
//					groupVisited = filter.get("groupVisited");
//					groupVisited = groupVisited.replaceAll(",", "','");
//
//					ticketList = lIncidentHistoryRepo.fetchTicketIdByGroupVisited(groupVisited);
//
//					String tickets = ticketList.stream().distinct().collect(Collectors.joining(","));
//
//					if (ticketList.size() > 0) {
//						filter.put("incidentID", tickets);
//					}
//					filter.remove("groupVisited");
//
//					payLoadNew = objMapper.writeValueAsString(filter);
//
//					mappedList = filteredJobList(payLoadNew);
//
//				}
//
//			}
//		} catch (Exception e) {
//			logger.error("Exception occured in filteredjoblistWithGroupVisited: " + e.getMessage(), e);
//
//		}
//		return mappedList;
//	}

	@Override
	public MappedVariableInstanceLogVO incidentClone(String authToken, String containerId, String processId,
			String clientCode, String requestPayload, String incidentId) {
		MappedVariableInstanceLogVO newTicket = new MappedVariableInstanceLogVO();
		MappedVariableInstanceLogVO ticketCreationData = new MappedVariableInstanceLogVO();
		List<IncidentAttachmentEntity> incAttachmentList = new ArrayList<IncidentAttachmentEntity>();
		List<IncidentAttachmentEntity> incNewAttachmentList = new ArrayList<IncidentAttachmentEntity>();
		UserDetailsSummary currentUser = userRepo.findByAuthToken(authToken);
		try {
			String processInstanceId = jbpmProcessServiceImpl.createProcessInstance(authToken, containerId, processId,
					clientCode, requestPayload);
			String taskInstanceId = jbpmProcessServiceImpl.getTaskId(authToken, processInstanceId);
			JSONObject jobj = new JSONObject(taskInstanceId);
			JSONArray jArray = jobj.getJSONArray("task-summary");
			String newTaskID = jArray.getJSONObject(jArray.length() - 1).optString("task-id");
			MappedVariableInstanceLogVO oldTicketData = mappedVariableInstanceRepo.findByIncidentID(incidentId);
			ticketCreationData.setCategory(oldTicketData.getCategory());
			ticketCreationData.setSubCategory(oldTicketData.getSubCategory());
			ticketCreationData.setTitle(oldTicketData.getTitle());
			ticketCreationData.setUrgency(oldTicketData.getUrgency());
			ticketCreationData.setImpact(oldTicketData.getImpact());
			ticketCreationData.setPriority(oldTicketData.getPriority());
			ticketCreationData.setSource(oldTicketData.getSource());
			ticketCreationData.setSourceContact(oldTicketData.getSourceContact());
			ticketCreationData.setDescriptions(oldTicketData.getDescriptions());
			ticketCreationData.setAssignmentGroup(oldTicketData.getAssignmentGroup());
			ticketCreationData.setAssignTo(oldTicketData.getAssignTo());
			ticketCreationData.setComments("Cloned from "+incidentId);
			ticketCreationData.setModule(oldTicketData.getModule());
			ticketCreationData.setConfigurationItem(oldTicketData.getConfigurationItem());
			ticketCreationData.setCreatedBy(currentUser.getEmail());
			ticketCreationData.setCreatedByFullName(currentUser.getFullName());
			ticketCreationData.setMarkAsMajorIncident(oldTicketData.getMarkAsMajorIncident());
			ticketCreationData.setIncidentType(oldTicketData.getIncidentType());

			incAttachmentList = fetchIncidentAttchmentList(oldTicketData.getIncidentID());

			// Inserting the Attachment List
			if (incAttachmentList != null && incAttachmentList.size() > 0) {

				for (IncidentAttachmentEntity incAttach : incAttachmentList) {
					IncidentAttachmentEntity incAttachEntity = new IncidentAttachmentEntity();
					incAttachEntity.setAttachment(incAttach.getAttachment());
					incAttachEntity.setAttachmentName(incAttach.getAttachmentName());
					incAttachEntity.setIncidentID("");
					incNewAttachmentList.add(incAttachEntity);
				}
				ticketCreationData.setIncidentAttachList(incNewAttachmentList);
			}

			newTicket = completeTask(authToken, containerId, newTaskID, clientCode, processInstanceId,
					ticketCreationData);
		} catch (CustomValidationException e) {
			logger.error("Exception occured in incidentClone: " + e.getMessage(), e);
		}
		return newTicket;
	}


	public MappedVariableInstanceLogVO getIncidentDetailsById(String incidentId)
	{
		List<MappedVariableInstanceLogVO> list = new ArrayList<MappedVariableInstanceLogVO>();
		if(StringUtil.isNotNullNotEmpty(incidentId))
		{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("incidentID", incidentId);
				JSONObject js = new JSONObject(map);
				list = filteredJobList(js.toString());
				if(list!=null && list.get(0)!=null)
				{
					return list.get(0);
				}
		}
		
		return null;
	}
	
	public MappedVariableInstanceLogVO patchCheckAndUpdate(String incidentId, HashMap<String, Object> map) throws SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		System.out.println("inside patchCheckAndUpdate 1");
		if(map!=null && map.size()>0)
		{
			System.out.println("inside patchCheckAndUpdate 2");
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = getIncidentDetailsById(incidentId);
			
			if(lMappedVariableInstanceLogVO!=null)
			{
				System.out.println("inside patchCheckAndUpdate 3");
				Method methods[] = lMappedVariableInstanceLogVO.getClass().getMethods();
				for(String key: map.keySet())
				{
					System.out.println("inside patchCheckAndUpdate 4 - "+key);
					boolean isKeyMatched = false;
					for(Method m: methods)
					{
						String methodNameUPPER = m.getName().toUpperCase();
						String keyUPPER = key.toUpperCase();
						
						
						System.out.println("inside patchCheckAndUpdate 5 - "+m.getName());
						if (methodNameUPPER.contains(keyUPPER) && methodNameUPPER.contains("SET"))
						{
							System.out.println("inside patchCheckAndUpdate 6");
							isKeyMatched = true;
							
//							lMappedVariableInstanceLogVO.getClass().getMethod(m.getName()).setAccessible(true);
							Method method = lMappedVariableInstanceLogVO.getClass().getMethod(m.getName(), String.class);
							method.invoke(lMappedVariableInstanceLogVO, map.get(key));
							System.out.println("inside patchCheckAndUpdate 7");
							System.out.println("setting the value for "+key+" as "+map.get(key));
							break;
							
						}
					}
					
					if(!isKeyMatched)
					{
						return null;
					}
				}
				
				return lMappedVariableInstanceLogVO;
			}
		}
		
		
		
		return null;
	}
	
	public boolean isValidRequest()
	{
		return false;
	}

	public String getAssignToFromIncidentTicket(String incidentID) {
		String assignTo = mappedVariableInstanceRepo.findByIncidentID(incidentID).getAssignTo();
		System.out.println(" assignTo " +assignTo);
		return assignTo;
	}
	
	public void sendLink(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {
		try {
		
			String url = lConfigEntityCached.getValue("scheduler.api.url")+"/csat/sentNotification/"+lMappedVariableInstanceLogVO.getIncidentID();
			RestTemplate restTemplate = new RestTemplate();
			Map<String, String> params = new HashMap<String, String>();
			params.put("ticketid", lMappedVariableInstanceLogVO.getIncidentID());
			URI uri = UriComponentsBuilder.fromUriString(url).buildAndExpand(params).toUri();
			restTemplate.postForEntity(uri, HttpMethod.POST, String.class);

		} catch (Exception e) {
			logger.error("Exception Occurred in sendLink: " + e.getMessage(), e);
		}

	}
	
}
