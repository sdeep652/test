package com.tcts.foresight.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.ProblemNotificationEntity;
import com.tcts.foresight.entity.ProblemNotificationIncident;
import com.tcts.foresight.problemnotification.entity.ProblemConfigurationActionsEntity;
import com.tcts.foresight.problemnotification.entity.ProblemNotificationCriteriaEntity;
import com.tcts.foresight.problemnotification.repository.ProblemNotificationCriteriaRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.IncidentHistoryRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.repository.ProblemNotificationIncidentRepo;
import com.tcts.foresight.repository.ProblemNotificationRepo;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.StringUtil;





@Service
public class ProblemNotificationImpl {	
	
	Logger logger = LoggerFactory.getLogger(ProblemNotificationImpl.class);
	
	@Autowired
	IncidentHistoryRepo lIncidentHistoryRepo;
	
	@Autowired
	ProblemNotificationRepo problemNotificationRepo;
	
	@Autowired
	private GroupDetailsRepository groupDtlsRepo;
	
	@Autowired
	private MappedVariableInstanceRepo lMappedVariableInstanceRepo;
	
	@Autowired
	ProblemNotificationIncidentRepo problemNotificationIncidentRepo;
	
	@Autowired
	private ProblemNotificationCriteriaRepo lProblemNotificationCriteriaRepo;
	
	@Autowired
	private ProblemNotificationRepo lProblemNotificationRepo;
	
	@Autowired
	private IncidentCreationServiceImpl lIncidentCreationServiceImpl;
	
	
	// will be called on closed  or from major incident closed.
	private ProblemNotificationEntity toSetNotification(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {
		ProblemNotificationEntity notificationEntity = new ProblemNotificationEntity();
		System.out.println("=================" + lMappedVariableInstanceLogVO.toString());
		ProblemNotificationIncident problemNotificationIncident = new ProblemNotificationIncident();
		problemNotificationIncident.setIncidentID(lMappedVariableInstanceLogVO.getIncidentID());
		
		System.out.println("====problemNotificationIncident=============" + problemNotificationIncident.toString());
		notificationEntity.setIncidentIDList(Arrays.asList(problemNotificationIncident));			
		notificationEntity.setIsEnable("Y");
		System.out.println("=====notificationEntity============" + notificationEntity);
		return notificationEntity;
	}
	 
	
	// this method will be invoked only from closed status. and only for 1 incident.
	public void notificationForProblem(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {
		
		ProblemNotificationEntity notificationEntity = new ProblemNotificationEntity();
		boolean checkIncidentisPresent = false;
		checkIncidentisPresent = problemNotificationIncidentRepo.findByIncidentID(lMappedVariableInstanceLogVO.getIncidentID()).isPresent();
				
		 if(!(checkIncidentisPresent)) {
		boolean checkResolvedStatus = lIncidentHistoryRepo.findFirstByIncidentIDAndOldValue(
				lMappedVariableInstanceLogVO.getIncidentID(), Constant.INCIDENT_STATUS_RESOLVED).isPresent();

		try {

			if ((checkResolvedStatus)
					&& "true".equalsIgnoreCase(lMappedVariableInstanceLogVO.getMarkAsMajorIncident())) {

				notificationEntity = toSetNotification(lMappedVariableInstanceLogVO);

				notificationEntity.setNotification("Is Resolved Multiple Times And Is A Major Incident");

				problemNotificationRepo.save(notificationEntity);
			} else if ((checkResolvedStatus)
					&& (lMappedVariableInstanceLogVO.getMarkAsMajorIncident().equalsIgnoreCase("false")	|| lMappedVariableInstanceLogVO.getMarkAsMajorIncident() == null)) {
				notificationEntity = toSetNotification(lMappedVariableInstanceLogVO);
				notificationEntity.setNotification("Is Resolved Multiple Times");
				problemNotificationRepo.save(notificationEntity);

			} else if (lMappedVariableInstanceLogVO.getMarkAsMajorIncident().equalsIgnoreCase("true") && !(checkResolvedStatus)) {
				notificationEntity = toSetNotification(lMappedVariableInstanceLogVO);
				notificationEntity.setNotification("Is A Major Incident");
				problemNotificationRepo.save(notificationEntity);
				
			}else {
			}

		} catch (Exception e) {
			logger.error("notificationForProblem :: Exception occurred in Notification for Problem" + e.getMessage());
		}
		} else {
			logger.info("Incident Notification is Already Present :- " + lMappedVariableInstanceLogVO.getIncidentID());
		}
			 
		}
		
	
//	public boolean checkIncident(String incidentID) {
//		List<String> incidentList = problemNotificationRepo.findAll().stream().map(ProblemNotificationEntity::getIncidentID)
//				.collect(Collectors.toList());
//
//		if (incidentList.contains(incidentID)) {
//			return true;
//		} else {
//			return false;
//		
//
//	}
	
	
	public List<ProblemNotificationEntity> fetchAllNotificationByEmail(String emailid, String module) {

		// get all enabled notifications from the notification table.
		// query the incident table and fetch all the incidents using IN and
		// assignedToGroup IN emailId's groups)
		List<String> groupName = new ArrayList<String>();
		if(StringUtil.isNotNullNotEmpty(emailid ) && StringUtil.isNotNullNotEmpty(module )) 
		{
//		String gpd2 = groupDtlsRepo.findGroupNameByEmail(emailid);
			List<GroupSummary> gpd = groupDtlsRepo.fetchGroupListByEmailAndModule(emailid, module);
			for (GroupSummary gpd1 : gpd) {
				groupName.add(gpd1.getGroupName());
			}

			List<ProblemNotificationEntity> notificationEntity = new ArrayList<ProblemNotificationEntity>();
			List<ProblemNotificationEntity> notificationEntityDulpicateRemove = new ArrayList<ProblemNotificationEntity>();
			if (groupName.size() > 0) {
				notificationEntity = problemNotificationRepo.findByenableAndGroup("Y", groupName);
				Set<ProblemNotificationEntity> lProblemNotificationEntitySet = new HashSet<ProblemNotificationEntity>();
				lProblemNotificationEntitySet.addAll(notificationEntity);
				notificationEntityDulpicateRemove.addAll(lProblemNotificationEntitySet);
				return notificationEntityDulpicateRemove;

			} else {
				notificationEntity.clear();
				logger.info("There are no group assign to user" + emailid);
				return notificationEntity;
			}
		}
		logger.info("There Email Id is not present" + emailid);
		return null;
	}
	
	public List<ProblemNotificationEntity> fetchAllNotification() {

		List<ProblemNotificationEntity> notificationEntity = new ArrayList<ProblemNotificationEntity>();
		notificationEntity = problemNotificationRepo.findAll();
		return notificationEntity;

	}
	
	public List<ProblemNotificationEntity> fetchAllEnableNotification() {

		List<ProblemNotificationEntity> notificationEntity = new ArrayList<ProblemNotificationEntity>();
		notificationEntity = problemNotificationRepo.findByIsEnable("Y");
		return notificationEntity;

	}
	
	public ProblemNotificationEntity fetchAllNotificationById(Long id){
		
		ProblemNotificationEntity notificationEntity = new ProblemNotificationEntity();
		notificationEntity = problemNotificationRepo.findById(id) .orElseThrow(() -> new EntityNotFoundException());
		if(notificationEntity != null) {
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVO = new MappedVariableInstanceLogVO();
			for (ProblemNotificationIncident lProblemNotificationIncident : notificationEntity.getIncidentIDList()) {
				lMappedVariableInstanceLogVO = lMappedVariableInstanceRepo.findByIncidentID(lProblemNotificationIncident.getIncidentID());
				lProblemNotificationIncident.setPriority(lMappedVariableInstanceLogVO.getPriority());
				lProblemNotificationIncident.setCategory(lMappedVariableInstanceLogVO.getCategory());
				System.out.println("lMappedVariableInstanceLogVO ===="+lMappedVariableInstanceLogVO);
			}
			
		}
       
		
		
		return notificationEntity;
	}
	
	/*
	 * public List<ProblemNotificationEntity>
	 * fetchAllNotificationByIncidentId(List<String> incidentList) {
	 * 
	 * String whereClause = ""; for(String str: incidentList) {
	 * if(StringUtil.isNotNullNotEmpty(whereClause)) { whereClause = whereClause
	 * +",\""+str+"\""; }else { whereClause = whereClause +"{\""+str+"\""; }
	 * 
	 * }
	 * 
	 * whereClause = whereClause + "}";
	 * 
	 * List<ProblemNotificationEntity> notificationEntity = new
	 * ArrayList<ProblemNotificationEntity>(); notificationEntity =
	 * problemNotificationRepo.findByIsEnable("Y"); return notificationEntity;
	 * 
	 * }
	 */

	public ProblemNotificationEntity createOrUpdateNotification(ProblemNotificationEntity notificationEntity) {
		System.out.println("======================");
		return problemNotificationRepo.save(notificationEntity);
	}

	
//	public ProblemNotificationEntity createOrUpdateNotification(ProblemNotificationEntity notificationEntity) {
//		
//		ProblemNotificationEntity lNotificationEntity = problemNotificationRepo.findByIncidentID(notificationEntity.getIncidentID());
//		
//		if(!(lNotificationEntity == null)) {
//			lNotificationEntity.setIsEnable(notificationEntity.getIsEnable());
//			lNotificationEntity.setClosedDate(notificationEntity.getClosedDate());
//			lNotificationEntity.setGroupName(notificationEntity.getGroupName());
//			lNotificationEntity.setNotification(notificationEntity.getNotification());
//			return lNotificationEntity;
//		}else {
//			problemNotificationRepo.save(notificationEntity);
//			return notificationEntity;
//		}
//		
//	}
	
	
	private void createNotificationRecord(ProblemNotificationCriteriaEntity lProblemNotificationCriteriaEntity, List<MappedVariableInstanceLogVO> lMappedVariableInstanceLogVOList)
	{
		if (lProblemNotificationCriteriaEntity.getNoOfIncidents() != null && lMappedVariableInstanceLogVOList.size() > 0 && lMappedVariableInstanceLogVOList
				.size() >= Integer.valueOf(lProblemNotificationCriteriaEntity.getNoOfIncidents())) {
			
			
			//Insert new Records in Problem Notification Table
			ProblemNotificationEntity lProblemNotificationEntity=new ProblemNotificationEntity();
			lProblemNotificationEntity.setIsEnable("Y");
			lProblemNotificationEntity.setNotification("Are Meeting Problem Criteria");
			List<ProblemNotificationIncident> lProblemNotificationIncident= new ArrayList<ProblemNotificationIncident>();
			
			for(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO:lMappedVariableInstanceLogVOList) {
				ProblemNotificationIncident lProblemNotificationIncidentObject=new ProblemNotificationIncident();
				lProblemNotificationIncidentObject.setIncidentID(lMappedVariableInstanceLogVO.getIncidentID());
				lProblemNotificationIncidentObject.setIncidentCreationDate(lMappedVariableInstanceLogVO.getIncidentCreationDate());
				lProblemNotificationIncident.add(lProblemNotificationIncidentObject);
			}
			lProblemNotificationEntity.setIncidentIDList(lProblemNotificationIncident);
			System.out.println("2");
			lProblemNotificationRepo.save(lProblemNotificationEntity);
			
			
		}
	}
	
	public List<MappedVariableInstanceLogVO> checkAndCreateProblemNotification(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		// time 5 minutes
		// count 5
		
		//1) if in last 5 minutes, 5 tickets are created, create a notification.
		
		//2) if in last 5 minutes, 6 tickets are created, update the same notification with 6 tickets
		
		// 3) if in last 5 minutes, 4 tickets are created (3 are from previous notification, 1 is not there in the notification yet), then consider count =1
		
		
		
		

		List<MappedVariableInstanceLogVO> lMappedVariableInstanceLogVOList = null;
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		List<ProblemNotificationCriteriaEntity> problemNotificationCriteriaEntityList = null;
		Calendar currentDate = Calendar.getInstance();
		String incidentCreatioDate = lMappedVariableInstanceLogVONEW.getIncidentCreationDate();
		
		
		String timeLimitsForPreviusTickets = "";

		//problemNotificationCriteriaEntityList = lProblemNotificationCriteriaRepo.findAll();
		problemNotificationCriteriaEntityList = lProblemNotificationCriteriaRepo.findByIsActive("true");
//		System.out.println("problemNotificationCriteriaEntityList"+problemNotificationCriteriaEntityList.toString());

		if (problemNotificationCriteriaEntityList.size() > 0) {
			
			try {
				
				for (ProblemNotificationCriteriaEntity lProblemNotificationCriteriaEntity : problemNotificationCriteriaEntityList) {
					// for each criteria do below
					
					long ruleTimeInMillies = getRuleTimeInMillies(lProblemNotificationCriteriaEntity);
					Calendar previouseDate = Calendar.getInstance();
					previouseDate.setTimeInMillis(currentDate.getTimeInMillis() - ruleTimeInMillies);
					timeLimitsForPreviusTickets = String.valueOf(sdf.format(previouseDate.getTime()));
					
					
					System.out.println("incidentCreatioDate "+incidentCreatioDate);
					System.out.println("timeLimitsForPreviusTickets "+timeLimitsForPreviusTickets);

					lMappedVariableInstanceLogVOList = matchNotificationsRuleActions(lProblemNotificationCriteriaEntity,timeLimitsForPreviusTickets, incidentCreatioDate);
					
					if(lMappedVariableInstanceLogVOList == null || lMappedVariableInstanceLogVOList.size()==0)
					{
						continue ;
					}
					
					System.out.println("matchNotificationsRuleActions lMappedVariableInstanceLogVOList"+ lMappedVariableInstanceLogVOList.size());

					
					List<String> listOfAllFoundIncidents= new ArrayList<String>();
					List<String> listOfAllFoundIncidentsExceptCurrent= new ArrayList<String>();
					
					
					if(lMappedVariableInstanceLogVOList !=null && lMappedVariableInstanceLogVOList.size()>0) {
						
						for(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO:lMappedVariableInstanceLogVOList) {
							listOfAllFoundIncidents.add(lMappedVariableInstanceLogVO.getIncidentID());
							if(!lMappedVariableInstanceLogVO.getIncidentID().equals(lMappedVariableInstanceLogVONEW.getIncidentID()))
							{
								listOfAllFoundIncidentsExceptCurrent.add(lMappedVariableInstanceLogVO.getIncidentID());
							}
						}
					}
				System.out.println("listOfAllFoundIncidents "+listOfAllFoundIncidents);
				System.out.println("listOfAllFoundIncidentsExceptCurrent "+listOfAllFoundIncidentsExceptCurrent);
				System.out.println("lProblemNotificationCriteriaEntity.getNoOfIncidents() "+lProblemNotificationCriteriaEntity.getNoOfIncidents());
					//get Records from Problem Notification Entity if Incidents exist in Table
					ProblemNotificationEntity lProblemNotificationEntityObject= new ProblemNotificationEntity();
					if(listOfAllFoundIncidentsExceptCurrent != null && listOfAllFoundIncidentsExceptCurrent.size()>0) {
						System.out.println("listOfAllFoundIncidents++" +listOfAllFoundIncidents);
						 lProblemNotificationEntityObject=lProblemNotificationRepo.getexistedIncidentIdRecords(listOfAllFoundIncidentsExceptCurrent);
					}
					
					
					
					
				if(lProblemNotificationEntityObject!=null && listOfAllFoundIncidentsExceptCurrent!=null && listOfAllFoundIncidentsExceptCurrent.size()>0)
				{
					for(ProblemNotificationIncident lProblemNotificationIncident: lProblemNotificationEntityObject.getIncidentIDList())
					{
						System.out.println("lProblemNotificationEntityObject.getIncidentID - "+lProblemNotificationIncident.getIncidentID());
					}
					System.out.println("lProblemNotificationEntityObject.getIncidentIDList().size() - "+lProblemNotificationEntityObject.getIncidentIDList().size());
					System.out.println("listOfAllFoundIncidentsExceptCurrent.size() - "+listOfAllFoundIncidentsExceptCurrent.size());
					
					if(lProblemNotificationEntityObject.getIncidentIDList().size()==listOfAllFoundIncidentsExceptCurrent.size())
					{
						System.out.println("updating the previous notification...");
						// update the previous notification
						if(listOfAllFoundIncidentsExceptCurrent.size()>=Integer.valueOf(lProblemNotificationCriteriaEntity.getNoOfIncidents()))
						{
							System.out.println("updating the previous notification... 2 ");
							ProblemNotificationIncident lProblemNotificationIncident=new ProblemNotificationIncident();
							lProblemNotificationIncident.setIncidentID(lMappedVariableInstanceLogVONEW.getIncidentID());
							lProblemNotificationIncident.setIncidentCreationDate(lMappedVariableInstanceLogVONEW.getIncidentCreationDate());

							lProblemNotificationEntityObject.getIncidentIDList().add(lProblemNotificationIncident);
							
							lProblemNotificationRepo.save(lProblemNotificationEntityObject);
							System.out.println("previous notification updated");
							
						}
						
					}else {
						
						//create new notification
						System.out.println("old notification time is not matched, new notification need to be created");
						List<MappedVariableInstanceLogVO> updatedMappedVariableInstanceLogVOList = new ArrayList<MappedVariableInstanceLogVO>();
						
						for(MappedVariableInstanceLogVO item :lMappedVariableInstanceLogVOList)
						{
							boolean considerCurrentRecord = true;
							
							for(ProblemNotificationIncident lProblemNotificationIncident : lProblemNotificationEntityObject.getIncidentIDList())
							{
								if(lProblemNotificationIncident.getIncidentID().equals(item.getIncidentID()))
								{
									considerCurrentRecord = false;
									break;
								}
							}
							
							if(considerCurrentRecord)
							{
								updatedMappedVariableInstanceLogVOList.add(item);
							}
							
						}
						System.out.println("updatedMappedVariableInstanceLogVOList.size() "+updatedMappedVariableInstanceLogVOList.size());
						createNotificationRecord(lProblemNotificationCriteriaEntity, updatedMappedVariableInstanceLogVOList);
						
					}
				}else {
					
					//create the new notification
					createNotificationRecord(lProblemNotificationCriteriaEntity, lMappedVariableInstanceLogVOList);
				}
					
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			
		}
		return lMappedVariableInstanceLogVOList;

	}
	
		

	private int getRuleTimeInMillies(ProblemNotificationCriteriaEntity lProblemNotificationCriteriaEntity) {

		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
	//	String problemTargetTime = "";
		if (StringUtil.isNotNullNotEmpty(lProblemNotificationCriteriaEntity.getTimeInDays())) {
			days = Integer.parseInt(lProblemNotificationCriteriaEntity.getTimeInDays());
	//		problemTargetTime = lProblemNotificationCriteriaEntity.getTimeInDays() + " Days";
		}

		if (StringUtil.isNotNullNotEmpty(lProblemNotificationCriteriaEntity.getTimeInHours())) {
			
			hours = Integer.parseInt(lProblemNotificationCriteriaEntity.getTimeInHours());
			
//			if (StringUtil.isNotNullNotEmpty(problemTargetTime)) {
//				problemTargetTime = problemTargetTime + ", " + lProblemNotificationCriteriaEntity.getTimeInHours()
//					+ " Hours";
//			} else {
//				problemTargetTime = lProblemNotificationCriteriaEntity.getTimeInDays() + " Days";
//			}

		}

		if (StringUtil.isNotNullNotEmpty(lProblemNotificationCriteriaEntity.getTimeInMins())) {
			
			minutes = Integer.parseInt(lProblemNotificationCriteriaEntity.getTimeInMins());
			
//			if (StringUtil.isNotNullNotEmpty(problemTargetTime)) {
//				problemTargetTime = problemTargetTime + ", " + lProblemNotificationCriteriaEntity.getTimeInMins()
//						+ " Minutes";
//			} else {
//				problemTargetTime = lProblemNotificationCriteriaEntity.getTimeInMins() + " Minutes";
//			}
		}

		if (StringUtil.isNotNullNotEmpty(lProblemNotificationCriteriaEntity.getTimeInSecs())) {
			seconds = Integer.parseInt(lProblemNotificationCriteriaEntity.getTimeInSecs());

//			if (StringUtil.isNotNullNotEmpty(problemTargetTime)) {
//				problemTargetTime = problemTargetTime + ", " + lProblemNotificationCriteriaEntity.getTimeInSecs()
//						+ " Seconds";
//			} else {
//				problemTargetTime = lProblemNotificationCriteriaEntity.getTimeInSecs() + " Seconds";
//			}
		}

		// testing
//		days = 28;
//		hours = 0;
//		minutes = 0;
//		seconds = 0;
		// testing ends
		
		int daysInSeconds = days * 24 * 60 * 60;
		int hoursInSeconds = hours * 60 * 60;
		int minutesInSeconds = minutes * 60;
		int totalSeconds = daysInSeconds + hoursInSeconds + minutesInSeconds + seconds;
		return totalSeconds * 1000;

	}

	private List<MappedVariableInstanceLogVO> matchNotificationsRuleActions(
			ProblemNotificationCriteriaEntity ProblemNotificationCriteriaEntitylist, String timeLimitsForPreviusTickets,String incidentCreatioDate) {

		// logger.info("matchSLAActions - 1");

		List<MappedVariableInstanceLogVO> listofIncidents = null;
		if (ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList() != null
				&& ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList().size() > 0) {
			String query = "select * from mapped_variable_instance_log where created_date BETWEEN '" + timeLimitsForPreviusTickets
					+ "' AND '" + incidentCreatioDate + "' AND ";
			if (ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList() != null
					&& ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList().size() > 0) {
				// logger.info("matchSLAActions - 2");
				Collections.sort(ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList());
			}
			// logger.info("matchSLAActions - 3");
			for (ProblemConfigurationActionsEntity lProblemNotificationCriteriaEntity : ProblemNotificationCriteriaEntitylist.getProblemConfigurationActionsEntityList()) {
				List<String> result = null;
				String columnValue = null;
				String columnName = lProblemNotificationCriteriaEntity.getConditionOn();

				// Hardcoded
				if (columnName.equalsIgnoreCase("subcategory")) {
					columnName = "sub_category";
				}
				if (lProblemNotificationCriteriaEntity.getValue().contains(",")) {

					result = Arrays.asList(lProblemNotificationCriteriaEntity.getValue().split("\\s*,\\s*"));
					// logger.info("matchSLAActions - 4");

					String inClause = "";
					for (String str : result) {

						inClause = inClause + "'" + str + "',";

					}

					if (inClause.endsWith(",")) {
						inClause = inClause.substring(0, inClause.length() - 1);
					}
					query = query + columnName + " IN (" + inClause + ")" + " AND ";

				} else {
					columnValue = lProblemNotificationCriteriaEntity.getValue();
					query = query + columnName + "='" + columnValue + "' AND ";
				}
				String operator = lProblemNotificationCriteriaEntity.getOperator();
				// logger.info("operator "+operator);

				if (StringUtil.isNotNullNotEmpty(operator)) {
					//query = query + " " + operator + " ";
				}

				// logger.info("columnName =------------" +columnName);
				// logger.info("query: "+query);

			}
			System.out.println("query1 ====" + query);
			// logger.info("matchSLAActions - 5");
			
			query = query.trim();
			if (query.endsWith("AND")) {

				query = query.substring(0, query.length() - 3);
			}
			
			// logger.info("query " + query);
			query=query+" Order by created_date ASC";
			System.out.println("query ====" + query);
			
			listofIncidents = lIncidentCreationServiceImpl.findByQuery(query);

//			if (listofIncidents != null && listofIncidents.size() > 0) {
////				isSLAConditionMatched = true;
//				matchedActions = ProblemNotificationCriteriaEntitylist;
//			}

		}

		return listofIncidents;
	}


}
