package com.tcts.foresight.service.impl.problem;


import java.lang.reflect.Method;
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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.client.JBPMClientConfig;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.problem.ProblemAttachmentEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemHistoryEntity;
import com.tcts.foresight.entity.problem.ProblemHistoryUpdatedValues;
import com.tcts.foresight.entity.problem.ProblemIncidentEntity;
import com.tcts.foresight.exception.CustomValidationException;
import com.tcts.foresight.jbpm.db.JBPMRowMapper;
import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.repository.problem.KnownErrorRepo;
import com.tcts.foresight.repository.problem.ProblemAttachmentRepo;
import com.tcts.foresight.repository.problem.ProblemDetailsRepo;
import com.tcts.foresight.repository.problem.ProblemHistoryRepo;
import com.tcts.foresight.repository.problem.ProblemIncidentRepo;
import com.tcts.foresight.service.impl.JbpmProcessServiceImpl;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.DateUtil;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.ObjectDiffUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class ProblemManagementImpl {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${RHPAM_DB_URL}")
	private String foresightURL;

	@Value("${RHPAM_DB_USER}")
	private String foresightUser;

	@Value("${RHPAM_DB_PASSWORD}")
	private String foresightPassword;

	@Autowired
	private JBPMClientConfig jbpmClient;

	@Autowired
	ProblemAttachmentRepo problemAttachmentRepo;

	@Autowired
	@Qualifier("auxDataSource")
	private DataSource auxDataSource;

	@Autowired
	private ProblemDetailsRepo problemDetailsRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;

	@Autowired
	private ProblemHistoryRepo problemHistoryRepo;

	@Autowired
	ProblemDetailsRepo lProblemDetailsRepo;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private ProblemIncidentRepo problemIncidentRepo;
	
	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;

	@Autowired
	private ObjectDiffUtil lObjectDiffUtil;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JbpmProcessServiceImpl jbpmProcessServiceImpl;
	
	@Autowired
	KnownErrorRepo lKnownErrorRepo;
	
	@Autowired
	KnownErrorManagementImpl knownErrorManagementImpl;
	
	List<ProblemAttachmentEntity> currentAttachementList = new ArrayList<ProblemAttachmentEntity>();

	public ProblemDetailsEntity completeTask(String authToken, String containerId, String taskInstanceId, String client,
			String processInstanceId, ProblemDetailsEntity jsonPayload) throws Exception {
		ProblemDetailsEntity returnProblemDetailsEntity = null;

		ProblemDetailsEntity problemVariableInstanceLogVOOLD = null;
		ProblemDetailsEntity problemVariableInstanceLogVOOLDClone = null;
		ProblemDetailsEntity problemVariableInstanceLogVONEW = null;
		if(jsonPayload.getProblemAttachmentList() !=null) {
			System.out.println("jsonPayload.getProblemAttachmentList()======"+jsonPayload.getProblemAttachmentList());
		currentAttachementList.addAll(jsonPayload.getProblemAttachmentList());
		jsonPayload.setProblemAttachmentList(null);
		}
		try {

			if (jsonPayload.getProblemID() != null) {
				problemVariableInstanceLogVOOLD = problemDetailsRepo.findByProblemID(jsonPayload.getProblemID());

				if (problemVariableInstanceLogVOOLD != null && problemVariableInstanceLogVOOLD.getProblemID() != null) {
					try {
						problemVariableInstanceLogVOOLDClone = (ProblemDetailsEntity) problemVariableInstanceLogVOOLD
								.clone();
						logger.info("====================	lMappedVariableInstanceLogVOOLDClone===================="
								+ objMapper.writeValueAsString(problemVariableInstanceLogVOOLDClone));
					} catch (Exception e) {
						logger.error("Exception occured lMappedVariableInstanceLogVOOLD: " + e.getMessage(), e);

					}
				}
			}

			if (("".equalsIgnoreCase(jsonPayload.getAssignmentGroup()) || jsonPayload.getAssignmentGroup() == null)
					&& ("".equalsIgnoreCase(jsonPayload.getAssignTo()) || jsonPayload.getAssignTo() == null)) {
				GroupSummary defaultModuleGrpDetails = groupDtlsRepo.findDistinctByDefaultModuleGroupContaining(jsonPayload.getModule().toUpperCase());
				if (defaultModuleGrpDetails != null) {
					jsonPayload.setAssignmentGroup(defaultModuleGrpDetails.getGroupName());
				} else {
					throw new CustomValidationException(
							"Default Problem Group is not found in database, please select the group in the form and submit");
				}
			}

			if (jsonPayload.getResolvedTimer() == null) {
				jsonPayload.setResolvedTimer(Constant.PROBLEM_MGMT_RESOLVED_TIMER);
			}

			// JBPM START TASK API
			String path = "containers/" + containerId + "/tasks/" + taskInstanceId + "/states/started";
			Response response = jbpmClient.putRequest(authToken, path, "");
			if ((response != null && (response.getStatus() == 200 || response.getStatus() == 201))) {
				// JBPM COMPLETE TASK API
				String lProblemDetailsEntityString = objMapper.writeValueAsString(jsonPayload);
				path = "containers/" + containerId + "/tasks/" + taskInstanceId + "/states/completed";
				response = jbpmClient.putRequest(authToken, path, lProblemDetailsEntityString);
				if ((response != null && (response.getStatus() == 200 || response.getStatus() == 201))) {

					
					returnProblemDetailsEntity = checkAndInsertInAuxDB(processInstanceId);
					jsonPayload.setProblemAttachmentList(currentAttachementList);
					currentAttachementList = new ArrayList<ProblemAttachmentEntity>();
					if (jsonPayload.getProblemAttachmentList() != null
							&& jsonPayload.getProblemAttachmentList().size() > 0) {
						List<ProblemAttachmentEntity> problemAttachmentList = checkAndInsertAttachmentInAuxDB(
								returnProblemDetailsEntity.getProblemID(), jsonPayload.getProblemAttachmentList());
						returnProblemDetailsEntity.setProblemAttachmentList(problemAttachmentList);
					}

					if (jsonPayload.getAssociatedIncidentList() != null
							&& jsonPayload.getAssociatedIncidentList().size() > 0) {
						List<ProblemIncidentEntity> associatedIncidentList = checkAndInsertAssociatedIncidentInAuxDB(
								returnProblemDetailsEntity.getProblemID(), jsonPayload.getAssociatedIncidentList());
						returnProblemDetailsEntity.setAssociatedIncidentList(associatedIncidentList);
					}
					if (returnProblemDetailsEntity.getProblemID() != null) {
						problemVariableInstanceLogVONEW = problemDetailsRepo
								.findByProblemID(returnProblemDetailsEntity.getProblemID());
						checkDifferenceAndCreateHistory(problemVariableInstanceLogVOOLDClone,
								problemVariableInstanceLogVONEW);

					}
					  // create a new impl file for KnownErrorManagementIMPL.
	                   
                    // call through autowire checkAndInsertInKnownError
					
					knownErrorManagementImpl.checkAndInsertInKnownError(jsonPayload);
				
				}

			}

		} catch (Exception e) {
			logger.error("Exception occured in complete task. Error details are below", e);
			throw e;
		}

		return returnProblemDetailsEntity;
	}

	public ProblemDetailsEntity checkAndInsertInAuxDB(String processInstanceId) {
		JSONObject newObject = new JSONObject();
		newObject.put("process-instance-id", processInstanceId);
		ProblemDetailsEntity lMappedVariableInstanceLogVO = null;

		List<ProblemDetailsEntity> lProblemDetailsEntityList = fetchDataFromJBPMDB(newObject.toString());
		for (ProblemDetailsEntity lProblemDetailsEntity : lProblemDetailsEntityList) {

			logger.info("Assign To Fetch Full Name:- " + lProblemDetailsEntity.getAssignTo());
			if (!"".equalsIgnoreCase(lProblemDetailsEntity.getAssignTo())) {
				lProblemDetailsEntity
						.setFullName(userRepo.findFullNameByEmail(lProblemDetailsEntity.getAssignTo()));
			}
			logger.info("mappedVariableInstanceLogVO JBPM mappedVariableInstanceLogVO.getStatus():- "
					+ lProblemDetailsEntity.getStatus());
			lMappedVariableInstanceLogVO = lProblemDetailsEntity;
			lMappedVariableInstanceLogVO = lProblemDetailsRepo.save(lProblemDetailsEntity);

		}
		return lMappedVariableInstanceLogVO;
	}

	private List<ProblemDetailsEntity> fetchDataFromJBPMDB(String jsonPayLoadMap) {
		List<ProblemDetailsEntity> mappedList = new ArrayList<ProblemDetailsEntity>();
		HashMap<String, String> filter = new HashMap<String, String>();
		filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
//		boolean dateRangeFilter = false;

		String finalQery = "select * from variableinstancelog where processinstanceid = '"
				+ filter.get("process-instance-id") + "'";
//		String populatedQueryOriginal = "select processinstanceid from variableinstancelog";

//		String internalSubQuery = populatedQueryOriginal;

//		if (filter.containsKey("start-date") || filter.containsKey("end-date")) {
//			dateRangeFilter = true;
//		}
//		String whereClause = " where ";
		if (filter != null && filter.size() > 0) {
			int size = 0;
			for (String key : filter.keySet()) {

				if (size == 0 && key.equalsIgnoreCase("process-instance-id")) {
//					internalSubQuery = populatedQueryOriginal + whereClause + "processinstanceid = '" + filter.get(key)
//							+ "'";
				}
			}
		}

//		finalQery = finalQery.replace("INNER_QUERY", internalSubQuery);
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
			mappedList = rowmapper.getProblemConvertedJBPMResponseAsList(lActualVariableInstanceLogVO);
			for (String key : filter.keySet()) {
				Iterator<ProblemDetailsEntity> itr = mappedList.iterator();
				// remove all even numbers
				while (itr.hasNext()) {
					ProblemDetailsEntity lMappedVariableInstanceLogVO = itr.next();

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

	public ProblemDetailsEntity getProblemDetailsById(String problemId) {
		ProblemDetailsEntity lProblemDetailsEntity = lProblemDetailsRepo.findByProblemID(problemId);
		return lProblemDetailsEntity;
	}

	public ProblemDetailsEntity patchCheckAndUpdate(String problemId, HashMap<String, Object> jsonPayload) {
		return null;
	}

	
	public List<ProblemAttachmentEntity> checkAndInsertAttachmentInAuxDB(String problemID,
			List<ProblemAttachmentEntity> problemAttachmentList) {
		List<ProblemAttachmentEntity> returnprobAttList = new ArrayList<ProblemAttachmentEntity>();

		for (ProblemAttachmentEntity probAttE : problemAttachmentList) {
			probAttE.setProblemID(problemID);
			probAttE.setByteAttachment(probAttE.getAttachment().getBytes());
			returnprobAttList.add(probAttE);
		}	
		return problemAttachmentRepo.saveAll(returnprobAttList);
	}

	public List<ProblemIncidentEntity> checkAndInsertAssociatedIncidentInAuxDB(String problemID,
			List<ProblemIncidentEntity> associatedIncidentList) {
		List<ProblemIncidentEntity> problemIncidentEntityList = new ArrayList<ProblemIncidentEntity>();
		//HashSet<ProblemIncidentEntity> lProblemIncidentEntity = new HashSet<ProblemIncidentEntity>();
		
		
		for (ProblemIncidentEntity PIncID : associatedIncidentList) {
			
			ProblemIncidentEntity p = problemIncidentRepo.findByIncidentIDAndProblemID(PIncID.getIncidentID(),problemID);
			if(p == null) {
				
				PIncID.setIncidentCategory(mappedVariableInstanceRepo.findByIncidentID(PIncID.getIncidentID()).getCategory());
				PIncID.setIncidentPriority(mappedVariableInstanceRepo.findByIncidentID(PIncID.getIncidentID()).getPriority());
				PIncID.setProblemID(problemID);
				problemIncidentEntityList.add(PIncID);
				
			}
		}
		//lProblemIncidentEntity.addAll(problemIncidentEntityList);
		return problemIncidentRepo.saveAll(problemIncidentEntityList);
	}

	// Adding the Problem Attachment List
	public List<ProblemAttachmentEntity> addAttachment(List<ProblemAttachmentEntity> problemAttachmentList) {
		return problemAttachmentRepo.saveAll(problemAttachmentList);
	}

	// Delete Problem Attachment By Id
	public void deleteAttachment(Long attachmentId) {
		problemAttachmentRepo.deleteById(attachmentId);
	}

	// Fetching All Problem Attachments
	public List<ProblemAttachmentEntity> fetchProblemAttchmentList(String problemid) {
		List<ProblemAttachmentEntity> lProblemAttachmentEntity = new ArrayList<ProblemAttachmentEntity>();
		
		for (ProblemAttachmentEntity problemAttachmentEntity : problemAttachmentRepo.findByProblemID(problemid)) {
			
			String attachment = new String(problemAttachmentEntity.getByteAttachment());
			problemAttachmentEntity.setAttachment(attachment);
			lProblemAttachmentEntity.add(problemAttachmentEntity);
			
		}
		
		return lProblemAttachmentEntity;
	}

	// Delete Problem Incidents By Id
	public void deleteProblemAssociatedIncidents(Long problemID) {
		problemIncidentRepo.deleteById(problemID);
	}

	// Fetching All Problem Incident List
	public List<ProblemIncidentEntity> fetchProblemAssociatedIncidents(String ticketId) {
		return problemIncidentRepo.findByProblemID(ticketId);
	}
	
	// Fetching All Problem Incident List based on relationType
	public List<ProblemIncidentEntity> fetchProblemAssociatedIncidentsByRelationType(String ticketId,
			String relationType) {
		return problemIncidentRepo.findByProblemIDAndRelationType(ticketId, relationType);
	}

	public List<ProblemDetailsEntity> problemList(String jsonPayLoadMap) {
		HashMap<String, String> queryMap = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> queryMapList = new HashMap<String, ArrayList<String>>();
		String query = "from ProblemDetailsEntity where ";
		List<ProblemDetailsEntity> mappedList = new ArrayList<ProblemDetailsEntity>();
		HashMap<String, String> filter = new HashMap<String, String>();

		if (jsonPayLoadMap != null && !jsonPayLoadMap.trim().equals("")) {
			filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);

			if (filter != null && filter.size() > 0) {
				for (String key : filter.keySet()) {

					String value = filter.get(key);

					// check and return for sql Injection
					if (value.toLowerCase().contains(" or ") || value.toLowerCase().contains(" and ")
							|| value.toLowerCase().contains("'or ") || value.toLowerCase().contains("'and ")

					) {
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
		
		for(ProblemDetailsEntity lProblemDetailsEntity : mappedList) {
			String problemid = lProblemDetailsEntity.getProblemID();
			try {
			List<ProblemIncidentEntity> problemIncidentEntity = new ArrayList<ProblemIncidentEntity>();
			if (StringUtil.isNotNullNotEmpty(problemid)) {
				problemIncidentEntity = fetchProblemAssociatedIncidents(problemid);
			}
			if(problemIncidentEntity!=null) {
			lProblemDetailsEntity.setAssociatedIncidentList(problemIncidentEntity);
			}
			
		}
			catch(Exception e) {
				logger.info("Exception error while fetching associated incident"+e.getMessage());
			}
			}
		
		
		return mappedList;
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

	@SuppressWarnings("unchecked")
	public List<ProblemDetailsEntity> findByQueryFiltered(String query, HashMap<String, String> queryMap,
			HashMap<String, ArrayList<String>> queryMapList) {

		Query mappedVariableQuery = entityManager.createQuery(query, ProblemDetailsEntity.class);
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

		List<ProblemDetailsEntity> mappedVariable = mappedVariableQuery.getResultList();
		return mappedVariable;
	}
	
	public String workAroundBroadCast(List<ProblemIncidentEntity> problemIncidentEntity , ProblemDetailsEntity problemDetailsEntity) {
		for(ProblemIncidentEntity list: problemIncidentEntity) {
			MappedVariableInstanceLogVO incidentID = mappedVariableInstanceRepo.findByIncidentID(list.getIncidentID());
			if(!(incidentID.getStatus().equals("Closed")) || !(incidentID.getStatus().equals("Cancelled"))) {
				incidentID.setWorkAround(problemDetailsEntity.getWorkAround());
				mappedVariableInstanceRepo.save(incidentID);
			}
			}
		
		return "True";		
	}

	private void checkDifferenceAndCreateHistory(ProblemDetailsEntity problemVariableInstanceLogVOOLD,
			ProblemDetailsEntity problemVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		if (problemVariableInstanceLogVOOLD == null) {
			long initZeroTimeTaken = 0l;
			ProblemHistoryEntity problemHistoryEntity = new ProblemHistoryEntity();
			problemHistoryEntity.setUpdatedBy(problemVariableInstanceLogVONEW.getCreatedByFullName());
			problemHistoryEntity.setUpdatedDate(problemVariableInstanceLogVONEW.getProblemCreationDate());
			problemHistoryEntity.setComments(problemVariableInstanceLogVONEW.getComments());
			problemHistoryEntity.setProblemID(problemVariableInstanceLogVONEW.getProblemID());
			problemHistoryEntity.setMilestone("Problem Created");// by
			problemHistoryEntity.setTimeTaken(initZeroTimeTaken);
			// "+lMappedVariableInstanceLogVONEW.getCreatedByFullName());
			problemHistoryRepo.save(problemHistoryEntity);
		} else {
			List<ProblemHistoryEntity> problemHistoryEntityList = lObjectDiffUtil
					.checkProblemDifference(problemVariableInstanceLogVOOLD, problemVariableInstanceLogVONEW);
			if (problemHistoryEntityList != null && problemHistoryEntityList.size() > 0) {
				for (ProblemHistoryEntity problemHistoryEntity : problemHistoryEntityList) {
					if (problemHistoryEntity != null) {
						problemHistoryEntity.setUpdatedBy(problemVariableInstanceLogVONEW.getLastUpdatedBy());
						problemHistoryEntity.setUpdatedDate(problemVariableInstanceLogVONEW.getLastUpdatedDate());
						problemHistoryEntity.setComments(problemVariableInstanceLogVONEW.getComments());
						problemHistoryEntity.setProblemID(problemVariableInstanceLogVONEW.getProblemID());
						problemHistoryEntity.setMilestone("Problem Updated");// by
						// "+lMappedVariableInstanceLogVONEW.getLastUpdatedBy());
						// Setting Time Taken in MilliSeconds
						Calendar Start = DateUtil.stringToCal(problemVariableInstanceLogVOOLD.getProblemCreationDate(),
								sdf);
						if (StringUtil.isNotNullNotEmpty(problemVariableInstanceLogVOOLD.getLastUpdatedDate())) {
							Start = DateUtil.stringToCal(problemVariableInstanceLogVOOLD.getLastUpdatedDate(), sdf);
						}
						Calendar End = DateUtil.stringToCal(problemVariableInstanceLogVONEW.getLastUpdatedDate(), sdf);
						problemHistoryEntity.setTimeTaken(End.getTimeInMillis() - Start.getTimeInMillis());
					}
				}

				problemHistoryRepo.saveAll(problemHistoryEntityList);
			}

		}

	}

	public List<ProblemHistoryEntity> fetchAllProblemHistory(String problemID) {

		List<ProblemHistoryEntity> finalHistory = new ArrayList<ProblemHistoryEntity>();

		List<ProblemHistoryEntity> list = problemHistoryRepo.findByticketID(problemID);

		HashMap<String, List<ProblemHistoryEntity>> map = new HashMap<String, List<ProblemHistoryEntity>>();

		if (list != null && list.size() > 0)

		{

			for (ProblemHistoryEntity history : list)

			{

				if (map.containsKey(history.getUpdatedDate()))

				{

					List<ProblemHistoryEntity> filteredList = map.get(history.getUpdatedDate());

					filteredList.add(history);

					map.put(history.getUpdatedDate(), filteredList);

				} else {
					List<ProblemHistoryEntity> filteredList = new ArrayList<ProblemHistoryEntity>();

					filteredList.add(history);

					map.put(history.getUpdatedDate(), filteredList);

				}

			}

			for (String key : map.keySet())

			{

				ProblemHistoryEntity problemHistoryEntity = null;

				List<ProblemHistoryEntity> value = map.get(key);

				problemHistoryEntity = value.get(0);

				List<ProblemHistoryUpdatedValues> problemHistoryUpdatedValuesList = new ArrayList<ProblemHistoryUpdatedValues>();

				for (ProblemHistoryEntity history : value)

				{

					ProblemHistoryUpdatedValues problemHistoryUpdatedValues = new ProblemHistoryUpdatedValues();

					problemHistoryUpdatedValues.setFieldName(history.getFieldName());

					problemHistoryUpdatedValues.setOldValue(history.getOldValue());

					problemHistoryUpdatedValues.setNewValue(history.getNewValue());

					problemHistoryUpdatedValuesList.add(problemHistoryUpdatedValues);

				}

				problemHistoryEntity.setProblemHistoryUpdatedValuesList(problemHistoryUpdatedValuesList);

				finalHistory.add(problemHistoryEntity);

			}

		}

		Collections.sort(finalHistory);

		return finalHistory;

	}
	
	public HashMap<String, String> assignToMe(String authToken, String containerId,
			List<ProblemDetailsEntity> jsonPayload, String clientCode) {
		List<ProblemDetailsEntity> jsonList = jsonPayload;
		HashMap<String, String> responseMap = new HashMap<String, String>();
		 logger.info("List<ProblemDetailsEntity> jsonList" + jsonList);
		try {
			for (ProblemDetailsEntity mappedList : jsonList) {
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
					 logger.info("Hey this is your task-id-------" + taskInstanceId);
					 logger.info("mappedList---------" + mappedList);

					ProblemDetailsEntity problemDetailsEntity = completeTask(authToken, containerId,
							taskInstanceId, clientCode, processInstanceId, mappedList);
					if (responseMap.containsKey("successProblems")) {
						String currentValue = responseMap.get("successProblems");
						currentValue = currentValue + ", " + mappedList.getProblemID();
						responseMap.put("successProblems", currentValue);
					} else {
						responseMap.put("successProblems", mappedList.getProblemID());
					}
				} catch (Exception e) {
					logger.error("-----Exception occurred in assignToMe-------" + e.getMessage(), e);
					if (responseMap.containsKey("failedProblems")) {
						String currentValue = responseMap.get("failedProblems");
						currentValue = currentValue + ", " + mappedList.getProblemID();
						responseMap.put("failedProblems", currentValue);
					} else {
						responseMap.put("failedProblems", mappedList.getProblemID());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occur while in assignToMe " + e.getMessage(), e);
			return null;
		}
		return responseMap;
	}

	public HashMap<String, List<String>> fetchStatusVisited(String problemID) {
		List<String> statusList = new ArrayList<String>();
		HashMap<String, List<String>> statusVisited = new HashMap<String, List<String>>();
		ProblemDetailsEntity problemInfo = lProblemDetailsRepo.findByProblemID(problemID);
		List<ProblemHistoryEntity> problemHistoryEntity = problemHistoryRepo.findAllStatusById(problemID);

		if (problemHistoryEntity.size()>0) {
			for (ProblemHistoryEntity problemList : problemHistoryEntity) {
				statusList.add(problemList.getOldValue());
				statusList.add(problemList.getNewValue());
			}
		}
			statusList.add(Constant.PROBLEM_STATUS_NEW); // By default Ticket Lifecycle starts with new
			List<String> finalList = statusList.stream().distinct().collect(Collectors.toList());
			finalList.remove(problemInfo.getStatus());
			statusVisited.put("statusVisited", finalList);
			return statusVisited;
		
		
	}
	
	public void saveWordAround(ProblemDetailsEntity jsonPayload) {
		List<String> listOfProblemID = problemDetailsRepo.findAll().stream().map(ProblemDetailsEntity::getProblemID)
				.collect(Collectors.toList());
		if (listOfProblemID.contains(jsonPayload.getProblemID())) {
			
				if (StringUtil.isNotNullNotEmpty(jsonPayload.getWorkAround())) {
					List<ProblemIncidentEntity> listofincident = problemIncidentRepo
							.findByProblemID(jsonPayload.getProblemID());
					if (!(listofincident.isEmpty())) {
						workAroundBroadCast(listofincident, jsonPayload);
						
					}
				}
		}
	}
	
	public String getAssignToFromProblemTicket(String problemID) {

		String assignTo = lProblemDetailsRepo.findByProblemID(problemID).getAssignTo();
		System.out.println(" assignTo " +assignTo);
		return assignTo;

	}
}
