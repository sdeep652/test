package com.tcts.foresight.service.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.StatusDetailsEntity;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.IncidentHistoryRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.service.IncidentReportService;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.DateUtil;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class IncidentReportServiceImpl implements IncidentReportService {

	Logger logger = LoggerFactory.getLogger(IncidentReportServiceImpl.class);



	@Autowired
	private IncidentHistoryRepo lIncidentHistoryRepo;

	@Autowired
	private IncidentCreationServiceImpl lIncidentCreationServiceImpl;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;

	@Autowired
	private StatusServiceImpl statusServiceImpl;

	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;

	private static ArrayList<String> getReportList = new ArrayList<String>();
	static {
		getReportList.add("status");
		getReportList.add("assignmentGroup");

	}

	private HashMap<String, Object> getTimeStayedReport(MappedVariableInstanceLogVO mapincident) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);


		try {
			HashMap<String, Object> finalHashMap = new HashMap<>();
			// finalHashMap.put("incidentId", incidentID);
			List<IncidentHistoryEntity> incidentHistoryEntity = lIncidentHistoryRepo.findByticketID(mapincident.getIncidentID());
			MappedVariableInstanceLogVO incidentCurrentInfo = mapincident;

			Method methods[] = incidentCurrentInfo.getClass().getMethods();

			List<String> groupList = new ArrayList<String>();
			groupList = groupDtlsRepo.findOnlyGroup().stream().map(GroupSummary::getGroupName)
					.collect(Collectors.toList());

			List<String> statusList = new ArrayList<String>();
			statusList = statusServiceImpl.getAllStatus().stream().map(StatusDetailsEntity::getStatus)
					.collect(Collectors.toList());

			for (IncidentHistoryEntity incident : incidentHistoryEntity) {

				if (incident != null && StringUtil.isNotNullNotEmpty(incident.getFieldName())
						&& StringUtil.isNotNullNotEmpty(incident.getOldValue())
						&& getReportList.contains(incident.getFieldName())) {

					if (finalHashMap.get(incident.getFieldName()) == null) {
						HashMap<String, Long> map = new HashMap<String, Long>();

						if (map.get(incident.getOldValue()) == null) {
							Long timeTaken = incident.getTimeTaken();
							if (timeTaken == null) {
								timeTaken = new Long(0);
							}
							map.put(incident.getOldValue(), timeTaken);
						} else if (map.get(incident.getOldValue()) != null) {
							Long existingTime = map.get(incident.getOldValue());
							map.put(incident.getOldValue(), incident.getTimeTaken() + existingTime);
						}
						finalHashMap.put(incident.getFieldName(), map);

					} else if (finalHashMap.get(incident.getFieldName()) != null) {
						HashMap<String, Long> map = (HashMap<String, Long>) finalHashMap.get(incident.getFieldName());

						if (map.get(incident.getOldValue()) == null) {
							Long timeTaken = incident.getTimeTaken();
							if (timeTaken == null) {
								timeTaken = new Long(0);
							}
							map.put(incident.getOldValue(), timeTaken);
						} else if (map.get(incident.getOldValue()) != null) {
							Long existingTime = map.get(incident.getOldValue());
							map.put(incident.getOldValue(), incident.getTimeTaken() + existingTime);
						}
						finalHashMap.put(incident.getFieldName(), map);

					}

				}

			}

			if (!Constant.INCIDENT_STATUS_CANCELLED.equals(incidentCurrentInfo.getStatus())
					|| !Constant.INCIDENT_STATUS_CLOSED.equals(incidentCurrentInfo.getStatus())) {

				for (Method method : methods) {

					String methodName = method.getName();

					// getting all the getmethod to get the current value of the object
					if (methodName.startsWith("getStatus") || methodName.startsWith("getAssignmentGroup")) {
						String subStringMethodName = methodName.substring(3, methodName.length()); // will remove get
						subStringMethodName = subStringMethodName.substring(0, 1).toLowerCase()
								+ subStringMethodName.substring(1); // put 1st letter in small letter and rest as is.

						// checking if key value is present in final map
						if (finalHashMap.containsKey(subStringMethodName)
								|| getReportList.contains(subStringMethodName)) {
							Method callingClassMEthod = incidentCurrentInfo.getClass().getMethod(methodName);
							String response = (String) callingClassMEthod.invoke(incidentCurrentInfo);

							// Calculating Live Date diff
							Calendar lastUpdateDate = DateUtil
									.stringToCal(incidentCurrentInfo.getIncidentCreationDate(),sdf);
							if (StringUtil.isNotNullNotEmpty(incidentCurrentInfo.getLastUpdatedDate())) {
								lastUpdateDate = DateUtil.stringToCal(incidentCurrentInfo.getLastUpdatedDate(),
										sdf);
							}
							Calendar calCurrentDate = Calendar.getInstance();
							long diff = calCurrentDate.getTimeInMillis() - lastUpdateDate.getTimeInMillis();
							// long seconds = Math.round(diff / 1000);

							if (StringUtil.isNotNullNotEmpty(response)) {

								HashMap<String, Long> map = (HashMap<String, Long>) finalHashMap
										.get(subStringMethodName);

								if (map != null) {

									if (map.containsKey(response)) {
										long existingTime = map.get(response);
										map.put(response, (existingTime + diff));

									} else {
										map.put(response, diff);
									}
									finalHashMap.put(subStringMethodName, map);
								} else {
									HashMap<String, Long> newMap = new HashMap<String, Long>();
									if (newMap.containsKey(response)) {
										long existingTime = newMap.get(response);
										newMap.put(response, (existingTime + diff));
									} else {
										newMap.put(response, diff);
									}
									finalHashMap.put(subStringMethodName, newMap);
								}

							}
						}
					}
				}

			}

			HashMap<String, Long> smap = (HashMap<String, Long>) finalHashMap.get("status");
			for (String sList : statusList) {
				if (!smap.containsKey(sList)) {
					long timeDiff = 0L;
					smap.put(sList, timeDiff);
				}
			}

			long currentTimeDiff = 0L;

			Calendar lastUpdateDate = DateUtil.stringToCal(incidentCurrentInfo.getIncidentCreationDate(),sdf);
			if (StringUtil.isNotNullNotEmpty(incidentCurrentInfo.getLastUpdatedDate())) {
				lastUpdateDate = DateUtil.stringToCal(incidentCurrentInfo.getLastUpdatedDate(),sdf);
			}
			Calendar creationDate = DateUtil.stringToCal(incidentCurrentInfo.getIncidentCreationDate(),sdf);
			Calendar calCurrentDate = Calendar.getInstance();

			HashMap<String, Long> gmap = (HashMap<String, Long>) finalHashMap.get("assignmentGroup");

			if (gmap == null) {
				HashMap<String, Long> newMap = new HashMap<String, Long>();

				switch (incidentCurrentInfo.getStatus()) {

				case Constant.INCIDENT_STATUS_CANCELLED:
					currentTimeDiff = lastUpdateDate.getTimeInMillis() - creationDate.getTimeInMillis();
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				case Constant.INCIDENT_STATUS_CLOSED:
					currentTimeDiff = DateUtil.dateDifference(incidentCurrentInfo.getIncidentCreationDate(),
							incidentCurrentInfo.getResolvedDate());
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				case Constant.INCIDENT_STATUS_RESOLVED:
					currentTimeDiff = DateUtil.dateDifference(incidentCurrentInfo.getIncidentCreationDate(),
							incidentCurrentInfo.getResolvedDate());
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				case Constant.INCIDENT_STATUS_IN_PROGRESS:
					currentTimeDiff = calCurrentDate.getTimeInMillis() - creationDate.getTimeInMillis();
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				case Constant.INCIDENT_STATUS_NEW:
					currentTimeDiff = calCurrentDate.getTimeInMillis() - creationDate.getTimeInMillis();
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				}
			} else {
				HashMap<String, Long> newMap = (HashMap<String, Long>) finalHashMap.get("assignmentGroup");

				switch (incidentCurrentInfo.getStatus()) {

				case Constant.INCIDENT_STATUS_CANCELLED:
					currentTimeDiff = lastUpdateDate.getTimeInMillis() - creationDate.getTimeInMillis();
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				case Constant.INCIDENT_STATUS_CLOSED:
					currentTimeDiff = DateUtil.dateDifference(incidentCurrentInfo.getIncidentCreationDate(),
							incidentCurrentInfo.getResolvedDate());
					newMap.put(incidentCurrentInfo.getAssignmentGroup(), currentTimeDiff);
					finalHashMap.put("assignmentGroup", newMap);
					break;

				}

			}

			HashMap<String, Long> igmap = (HashMap<String, Long>) finalHashMap.get("assignmentGroup");

			for (String gList : groupList) {
				if (!igmap.containsKey(gList)) {
					long timeDiff = 0L;
					igmap.put(gList, timeDiff);
				}

			}

			return finalHashMap;

		} catch (Exception e) {
			logger.error("Exception Occurred in time stayed report: " + e.getMessage(), e);
		}

		return null;
	}

	@Override
	public List<HashMap<String, Object>> getAllIncidentAnalysisReport(String jsonPayLoadMap) {

		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		List<HashMap<String, Object>> finalMap = new ArrayList<HashMap<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		try {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoadMap)) {

				mappedList = fetchAgeingReport(jsonPayLoadMap);

				for (MappedVariableInstanceLogVO inc : mappedList) {
					
					long incidentAgeing = 0;
					String calculatedAgeingTimeInMinutes="";
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("incidentID", inc.getIncidentID());
					map.put("incidentCreationDate", inc.getIncidentCreationDate());
					map.put("category", inc.getCategory());
					map.put("subCategory", inc.getSubCategory());
					map.put("priority", inc.getPriority());
					map.put("source", inc.getSource());
					map.put("status", inc.getStatus());
					map.put("incidentAgeing", inc.getIncidentAgeing());
					map.put("resolvedTimer", inc.getResolvedTimer());
					map.put("responseSlaBreach", inc.getResponseSlaBreach());
					map.put("resolutionSlaBreach", inc.getResolutionSlaBreach());
					map.put("incidentAnalysisDetails", getTimeStayedReport(inc));
					//added for All Attribute:-> karan
					map.put("parentTicketId", inc.getParentTicketId());
					map.put("configurationItem", inc.getConfigurationItem());
					map.put("resolutionMethod",inc.getResolutionMethod());
					map.put("resolutionType", inc.getResolutionType());
					map.put("statusRemark", inc.getStatusRemark());
					map.put("resolutionRemarks",inc.getResolutionRemarks());
					map.put("resolvedDate", inc.getResolvedDate());
					map.put("resolvedBy",inc.getResolvedBy());
					map.put("title", inc.getTitle());
					map.put("createdBy", inc.getCreatedBy());
					map.put("resolutionSlaBracket", inc.getResolutionSlaBracket() );
					map.put("incidentClosedDate", inc.getIncidentClosedDate());
					map.put("assignmentGroup", inc.getAssignmentGroup());
					map.put("createdByFullName", inc.getCreatedByFullName());
					map.put("lastUpdatedDate", inc.getLastUpdatedDate());
					map.put("lastUpdatedBy", inc.getLastUpdatedBy());
					
					//added totalIncidentAgeingInMinutes  Attribute:-> dhiraj
					Calendar currentDate = Calendar.getInstance();
					Calendar creationDate = DateUtil.stringToCal(inc.getIncidentCreationDate(),sdf);
					incidentAgeing = currentDate.getTimeInMillis() - creationDate.getTimeInMillis();
					calculatedAgeingTimeInMinutes=String.valueOf(incidentAgeing / 60000);
					map.put("totalIncidentAgeingInMinutes", calculatedAgeingTimeInMinutes);
					
				
					finalMap.add(map);
				}

			}
		} catch (Exception e) {
			logger.error("Exception Occurred in Multifetch Incident: " + e.getMessage(), e);
		}
		return finalMap;
	}

	@Override
	public Long getIncidentAgeing(String incidentID) {
		String createdDate = mappedVariableInstanceRepo.getIncidentAgeing(incidentID);
		String resolvedDate = mappedVariableInstanceRepo.getIncidentAgeings(incidentID);
		Long l = DateUtil.dateDifference(createdDate, resolvedDate);
		return l;
	}

	@Override
	public List<MappedVariableInstanceLogVO> fetchAgeingReport(String jsonPayLoadMap) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		
		HashMap<String, String> filter = new HashMap<String, String>();
		filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
		String groupName=filter.get("assignmentGroup");
		try {
			if (groupName != null) {
				if (groupName.contains(Constant.READ_ONLY_GROUP)) {
					ObjectMapper mapper = new ObjectMapper();
					filter.put("assignmentGroup", "!"+Constant.READ_ONLY_GROUP);
					jsonPayLoadMap = mapper.writeValueAsString(filter);
				} else {
					//				do nothing
				}

			} 
		} catch (Exception e) {
			
		}
		System.out.println("jsonPayLoadMap"+jsonPayLoadMap);

		List<MappedVariableInstanceLogVO> list = lIncidentCreationServiceImpl.filteredJobList(jsonPayLoadMap);
		//Dhiraj Adding totalIncidentAgingInMinutes column  

		
		// in response return two additional columns
		try {
			for (MappedVariableInstanceLogVO lMappedVariableInstanceLogVO : list) {
				if (lMappedVariableInstanceLogVO != null) {
					long incidentAgeing = 0;
					String calculatedAgeingTimeInMinutes="";
					String currentStatus = lMappedVariableInstanceLogVO.getStatus();
					if (Constant.INCIDENT_STATUS_NEW.equals(currentStatus)
							|| Constant.INCIDENT_STATUS_IN_PROGRESS.equals(currentStatus)
							|| Constant.INCIDENT_STATUS_ON_HOLD.equals(currentStatus)) {
						Calendar currentDate = Calendar.getInstance();
						Calendar creationDate = DateUtil.stringToCal(lMappedVariableInstanceLogVO.getIncidentCreationDate(),sdf);
						incidentAgeing = currentDate.getTimeInMillis() - creationDate.getTimeInMillis();
						lMappedVariableInstanceLogVO.setIncidentAgeing(incidentAgeing);
						 calculatedAgeingTimeInMinutes=String.valueOf(incidentAgeing / 60000);
						lMappedVariableInstanceLogVO.setTotalIncidentAgeingInMinutes(calculatedAgeingTimeInMinutes);
					} else if (Constant.INCIDENT_STATUS_RESOLVED.equals(currentStatus)
							|| Constant.INCIDENT_STATUS_CLOSED.equals(currentStatus)) {
						incidentAgeing = DateUtil.dateDifference(lMappedVariableInstanceLogVO.getIncidentCreationDate(),lMappedVariableInstanceLogVO.getResolvedDate());
						lMappedVariableInstanceLogVO.setIncidentAgeing(incidentAgeing);
						calculatedAgeingTimeInMinutes=String.valueOf(incidentAgeing / 60000);
						lMappedVariableInstanceLogVO.setTotalIncidentAgeingInMinutes(calculatedAgeingTimeInMinutes);
					} else if (Constant.INCIDENT_STATUS_CANCELLED.equals(currentStatus)) {
						incidentAgeing = DateUtil.dateDifference(lMappedVariableInstanceLogVO.getIncidentCreationDate(),lMappedVariableInstanceLogVO.getLastUpdatedDate());
						lMappedVariableInstanceLogVO.setIncidentAgeing(incidentAgeing);
						 calculatedAgeingTimeInMinutes=String.valueOf(incidentAgeing / 60000);
						lMappedVariableInstanceLogVO.setTotalIncidentAgeingInMinutes(calculatedAgeingTimeInMinutes);
					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in fetchAgeingReport: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public HashMap<String, List<String>> fetchAllIncidentCreatedResolvedUser() {

		HashMap<String, List<String>> list = new HashMap<>();

		// CreatedBy
		List<String> createdBy = mappedVariableInstanceRepo.getCreatedBy();
		list.put("createdBy", createdBy);
		// ResolvedBy
		List<String> resolvedBy = mappedVariableInstanceRepo.getResolvedBy();
		list.put("resolvedBy", resolvedBy);
		return list;

	}

	@Override
	public HashMap<String, String> fetchAvailableColumnDisplay() {
		HashMap<String,String> availableList=new HashMap<String, String>();
		availableList.put("incidentID","Incident ID");
		availableList.put("incidentCreationDate","Creation Date");
		availableList.put("parentTicketId","Parent Incident ID");
		availableList.put("category","Category");
		availableList.put("subCategory","Sub-Category");
		availableList.put("priority","Priority");
		availableList.put("status","Status");
		availableList.put("configurationItem","Configuration Item");
		availableList.put("resolutionMethod","Resolution Method");
		availableList.put("resolutionType","Resolution Type");
		availableList.put("statusRemark","Status Remark");
		availableList.put("resolvedDate","Resolved Date");
		availableList.put("createdBy","Created By");
		availableList.put("resolvedBy","Resolved By");
		availableList.put("responseSlaBreach","Response SLA Breach");
		availableList.put("resolutionSlaBreach","Resolution SLA Breach");
		availableList.put("resolutionSlaBracket","SLA Bracket");
		availableList.put("incidentAgeing","Total Ageing");
		availableList.put("assignmentGroup","Group Ageing");
		availableList.put("source","Source");
		availableList.put("resolutionRemarks","Resolution Remark");
		availableList.put("statusAgeing","Status Ageing");
		availableList.put("resolvedTimer","Auto-Closure Time(In Minutes)");
		availableList.put("totalIncidentAgeingInMinutes","Total Incident Ageing In Minutes");
		
		
		return availableList;
	}

}
