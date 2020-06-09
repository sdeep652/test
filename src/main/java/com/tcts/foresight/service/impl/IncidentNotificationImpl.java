package com.tcts.foresight.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcts.foresight.dao.NotificationReplaceVO;
import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.ProgressBasedNotificationEntity;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.IncidentHistoryRepo;
import com.tcts.foresight.repository.IncidentProgressNotificationRepo;
import com.tcts.foresight.util.StringUtil;

@Service
public class IncidentNotificationImpl {
	
	Logger logger = LoggerFactory.getLogger(IncidentNotificationImpl.class);
	
	
	@Autowired
	ConfigEntityCached lConfigEntityCached;
	
	@Autowired
	private IncidentProgressNotificationRepo incidentProgressNotificationRepo;
	
	@Autowired
	private IncidentHistoryRepo lIncidentHistoryRepo;
	
	@Autowired
	GroupDetailsRepository lGroupDetailsRepository;


	
	
	public void statusChangedNotification(MappedVariableInstanceLogVO mappedVariableInstaceLogVO, boolean flag) {

		try {
			// append Admin Email and phone no
			ProgressBasedNotificationEntity progressBasedNotificationEntity = incidentProgressNotificationRepo.findFirstByOrderByIdAsc();
			String finalEmailList = "";
			String phoneNumberList = "";
			if(mappedVariableInstaceLogVO.getStatusChangeCheckbox()==null) {
				mappedVariableInstaceLogVO.setStatusChangeCheckbox("false");
			}
			if(mappedVariableInstaceLogVO.getStatusChangeCheckbox().equalsIgnoreCase("true")) {
				if (StringUtil.isNotNullNotEmpty( mappedVariableInstaceLogVO.getEmail())) {
					finalEmailList = mappedVariableInstaceLogVO.getEmail();
				}
				if (StringUtil.isNotNullNotEmpty(mappedVariableInstaceLogVO.getSms())) {
					phoneNumberList = mappedVariableInstaceLogVO.getSms();
				}
			}

			if (StringUtil.isNotNullNotEmpty(progressBasedNotificationEntity.getEmailList())) {
				if (StringUtil.isNotNullNotEmpty(finalEmailList)) {
					finalEmailList = finalEmailList + ";" + progressBasedNotificationEntity.getEmailList();
				} else {
					finalEmailList = progressBasedNotificationEntity.getEmailList();
				}
			}
			if (StringUtil.isNotNullNotEmpty(progressBasedNotificationEntity.getPhoneNumberList())) {
				if (StringUtil.isNotNullNotEmpty(phoneNumberList)) {
					phoneNumberList = phoneNumberList + "," + progressBasedNotificationEntity.getPhoneNumberList();
				} else {
					phoneNumberList = progressBasedNotificationEntity.getPhoneNumberList();
				}
			}

			IncidentHistoryEntity lIncidentHistoryEntityList = lIncidentHistoryRepo.findByticketID1(mappedVariableInstaceLogVO.getIncidentID());
			//IncidentHistoryEntity lIncidentHistoryEntityList1 = lIncidentHistoryRepo.checkForOtherFieldUpdation(mappedVariableInstaceLogVO.getIncidentID());
			String finalPhoneNumberList1 = phoneNumberList;
			
			String finalEmailList1 = finalEmailList;
//			HashMap<String, String> requestBody = new HashMap<String, String>();

			Thread t = new Thread() {
				public void run() {
					if (flag) {
//						requestBody.put("action", "Created");
//						requestBody.put("incidentID", mappedVariableInstaceLogVO.getIncidentID());
//						requestBody.put("status", mappedVariableInstaceLogVO.getStatus());
//						requestBody.put("createdBy", mappedVariableInstaceLogVO.getCreatedBy());
//						requestBody.put("createdDate", mappedVariableInstaceLogVO.getIncidentCreationDate());
//						requestBody.put("priority", mappedVariableInstaceLogVO.getPriority());
//						requestBody.put("assignTo", mappedVariableInstaceLogVO.getAssignTo());
//						
//						requestBody.put("phoneNumberList", finalPhoneNumberList1);
//						requestBody.put("emailIdList", finalEmailList1);
						
						NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
						lNotificationReplaceVO.setAction("Created");
						lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
						lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
						lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
						lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
						lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
						lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
						lNotificationReplaceVO.setPhoneNumberList(finalPhoneNumberList1);
						lNotificationReplaceVO.setEmailIdList(finalEmailList1);
						
						
						try {
							String url = lConfigEntityCached.getValue("scheduler.api.url")+"/StatusBasedNotification/sent/";
							RestTemplate restTemplate = new RestTemplate();
							restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
							
							

						} catch (Exception e) {
							logger.error("Exception Occurred in sendLink: " + e.getMessage(), e);
						}

					}
					if (!flag) {
						String oldValue = "";
						if (mappedVariableInstaceLogVO.getStatus().equalsIgnoreCase("Closed")) {
							oldValue = "Resolved";
						} else {
							oldValue = lIncidentHistoryEntityList.getOldValue();
						}
						
						
						NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
						lNotificationReplaceVO.setAction("Updated");
						lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
						lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
						lNotificationReplaceVO.setNewStatus(mappedVariableInstaceLogVO.getStatus());
						
						lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
						lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
						lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
						lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
						lNotificationReplaceVO.setOldStatus(oldValue);
						lNotificationReplaceVO.setPhoneNumberList(finalPhoneNumberList1);
						lNotificationReplaceVO.setEmailIdList(finalEmailList1);
						
						
						String url = lConfigEntityCached.getValue("scheduler.api.url")+"/StatusBasedNotification/sent/";
						RestTemplate restTemplate = new RestTemplate();
						restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
						
					}
				}
			};
			
			t.start();
		} catch (Exception e) {
			logger.error("Exception occured in statusChangedNotification: " + e.getMessage(), e);
		}
	}

	
	public void sendNotificationToAssignTo(MappedVariableInstanceLogVO mappedVariableInstaceLogVO,boolean isCreated) {
	
		//If flag true the send create notification else update
				if(isCreated) {
//					HashMap<String, String> requestBody = new HashMap<String, String>();
					
//					requestBody.put("action", "Created");
//					requestBody.put("incidentID", mappedVariableInstaceLogVO.getIncidentID());
//					requestBody.put("status", mappedVariableInstaceLogVO.getStatus());
//					requestBody.put("createdBy", mappedVariableInstaceLogVO.getCreatedBy());
//					requestBody.put("createdDate", mappedVariableInstaceLogVO.getIncidentCreationDate());
//					requestBody.put("priority", mappedVariableInstaceLogVO.getPriority());
//					requestBody.put("assignmentGroup", mappedVariableInstaceLogVO.getAssignmentGroup());
//					requestBody.put("assignTo", mappedVariableInstaceLogVO.getAssignTo());	
//					requestBody.put("emailIdList", mappedVariableInstaceLogVO.getAssignTo());
				
					
					NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
					lNotificationReplaceVO.setAction("Created");
					lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
					lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
//					lNotificationReplaceVO.setNewStatus(mappedVariableInstaceLogVO.getStatus());
					
					lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
					lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
					lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
					lNotificationReplaceVO.setAssignmentGroup(mappedVariableInstaceLogVO.getAssignmentGroup());
					lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
					lNotificationReplaceVO.setEmailIdList(mappedVariableInstaceLogVO.getAssignTo());
					
						try {
							String url = lConfigEntityCached.getValue("scheduler.api.url")+"/notification/toAssignToUser/";
							System.out.println("URL"+url);
							RestTemplate restTemplate = new RestTemplate();
							restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
				
							}
						catch (Exception e) {
							logger.error("Exception Occurred in sendNotificationToAssignTo: " + e.getMessage(), e);
						}
				}else {
					
					
					
					NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
					lNotificationReplaceVO.setAction("Updated");
					lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
					lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
					lNotificationReplaceVO.setNewStatus(mappedVariableInstaceLogVO.getStatus());
					
					lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
					lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
					lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
					lNotificationReplaceVO.setAssignmentGroup(mappedVariableInstaceLogVO.getAssignmentGroup());
					lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
					lNotificationReplaceVO.setEmailIdList(mappedVariableInstaceLogVO.getAssignTo());
					
					
			
					try {
						String url = lConfigEntityCached.getValue("scheduler.api.url")+"/notification/toAssignToUser/";
						RestTemplate restTemplate = new RestTemplate();
						restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
			
						}
					catch (Exception e) {
						logger.error("Exception Occurred in sendNotificationToAssignTo: " + e.getMessage(), e);
					}
					
				}
		
	}
	
	public void sendNotificationGroupUsers(MappedVariableInstanceLogVO mappedVariableInstaceLogVO,boolean isCreated) {

	//If flag true the send create notification else update
				
				if(isCreated) {
					String finalEmailList = "";
					
					String groupId=lGroupDetailsRepository.findIDByGroupName(mappedVariableInstaceLogVO.getAssignmentGroup());
					List<String> userEmail=lGroupDetailsRepository.findEmailByGroupId(groupId);
					
					for(String email:userEmail) {
						finalEmailList=finalEmailList+email+";";
					}
					
					
					NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
					lNotificationReplaceVO.setAction("Created");
					lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
					lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
					lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
					lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
					lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
					lNotificationReplaceVO.setAssignmentGroup(mappedVariableInstaceLogVO.getAssignmentGroup());
					lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
					lNotificationReplaceVO.setEmailIdList(finalEmailList);
					
					
				
						try {
							String url = lConfigEntityCached.getValue("scheduler.api.url")+"/notification/toGroUpuser/";
							RestTemplate restTemplate = new RestTemplate();
							restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
				
							}
						catch (Exception e) {
							logger.error("Exception Occurred in sendNotificationGroupUsers: " + e.getMessage(), e);
						}
				}
				else {
					
					String finalEmailList = "";
					String groupId=lGroupDetailsRepository.findIDByGroupName(mappedVariableInstaceLogVO.getAssignmentGroup());
					List<String> userEmail=lGroupDetailsRepository.findEmailByGroupId(groupId);
					
					for(String email:userEmail) {
						finalEmailList=finalEmailList+email+";";
					}
					
					
					
					NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
					lNotificationReplaceVO.setAction("Updated");
					lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
					lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
					lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
					lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
					lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
					lNotificationReplaceVO.setAssignmentGroup(mappedVariableInstaceLogVO.getAssignmentGroup());
					lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
					lNotificationReplaceVO.setEmailIdList(finalEmailList);
				
						try {
							String url = lConfigEntityCached.getValue("scheduler.api.url")+"/notification/toGroUpuser/";
							RestTemplate restTemplate = new RestTemplate();
							restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);
				
							}
						catch (Exception e) {
							logger.error("Exception Occurred in sendNotificationGroupUsers: " + e.getMessage(), e);
						}
				}
	}
	
	
	public void checkAndSendNotifications(MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLDClone,MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW)
	{
		
		Thread t = new Thread() {
			public void run() {
				if(lConfigEntityCached.getValue("email.incident.created.assigend.to.group.notification").equalsIgnoreCase("Yes")) {
					if(lMappedVariableInstanceLogVOOLDClone ==null) {
						//group created notification
						sendNotificationGroupUsers(lMappedVariableInstanceLogVONEW,true);
					}else {
						//group Updated notification
						if (!lMappedVariableInstanceLogVOOLDClone.getAssignmentGroup().equalsIgnoreCase(lMappedVariableInstanceLogVONEW.getAssignmentGroup())) {
							sendNotificationGroupUsers(lMappedVariableInstanceLogVONEW,false);
						}
					}
				}
				if(lConfigEntityCached.getValue("email.incident.assigned.to.notification").equalsIgnoreCase("Yes")) {
					if(lMappedVariableInstanceLogVOOLDClone ==null) {
						//assignTo created notification
						sendNotificationToAssignTo(lMappedVariableInstanceLogVONEW,true);
					}else {
						//assignTo Updated notification
						if (StringUtil.isNotNullNotEmpty(lMappedVariableInstanceLogVONEW.getAssignTo()) && !lMappedVariableInstanceLogVOOLDClone.getAssignTo().equalsIgnoreCase(lMappedVariableInstanceLogVONEW.getAssignTo())) {
							sendNotificationToAssignTo(lMappedVariableInstanceLogVONEW,false);
						}
					}
				}
			}
		};
		t.start();
		
		
	}


	public void closedNotificationToCreator(MappedVariableInstanceLogVO mappedVariableInstaceLogVO) {
		
		
		
		
		NotificationReplaceVO lNotificationReplaceVO = new NotificationReplaceVO();
		lNotificationReplaceVO.setAction("Closed");
		lNotificationReplaceVO.setIncidentID(mappedVariableInstaceLogVO.getIncidentID());
		lNotificationReplaceVO.setStatus(mappedVariableInstaceLogVO.getStatus());
		lNotificationReplaceVO.setCreatedBy(mappedVariableInstaceLogVO.getCreatedBy());
		lNotificationReplaceVO.setCreatedDate(mappedVariableInstaceLogVO.getIncidentCreationDate());
		lNotificationReplaceVO.setPriority(mappedVariableInstaceLogVO.getPriority());
		lNotificationReplaceVO.setAssignmentGroup(mappedVariableInstaceLogVO.getAssignmentGroup());
		lNotificationReplaceVO.setAssignTo(mappedVariableInstaceLogVO.getAssignTo());
		lNotificationReplaceVO.setEmailIdList(mappedVariableInstaceLogVO.getCreatedBy());

		try {
			String url = lConfigEntityCached.getValue("scheduler.api.url")+"/notification/closedNotificationToCreator/";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(url, lNotificationReplaceVO, NotificationReplaceVO.class);

			}
		catch (Exception e) {
			logger.error("Exception Occurred in closedNotificationToCreator: " + e.getMessage(), e);
		}
	}
}
